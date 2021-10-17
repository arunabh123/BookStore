package com.getir.demo.bookstore.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.getir.demo.bookstore.exceptions.InsufficientStockException;
import com.getir.demo.bookstore.exceptions.OrderNotFoundException;
import com.getir.demo.bookstore.models.Book;
import com.getir.demo.bookstore.models.Customer;
import com.getir.demo.bookstore.models.Order;
import com.getir.demo.bookstore.models.OrderDetail;
import com.getir.demo.bookstore.models.OrderRequest;
import com.getir.demo.bookstore.models.OrderStatus;
import com.getir.demo.bookstore.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private BookService bookService;

	@Autowired
	private OrderRepository orderRepo;
	
	@Transactional
	public synchronized Order placeOrder(List<OrderRequest> orderRequests, Customer customer) {
		this.isInventoryAvailable(orderRequests);
		Order order = this.createOrder(orderRequests, customer);
		try {
			this.updateInventory(order);
			order.setStatus(OrderStatus.SUCCESS);
		} catch (Exception e) {
			order.setStatus(OrderStatus.FAILED);
		} finally {
			orderRepo.save(order);
		}
		return order;
	}

	private boolean isInventoryAvailable(List<OrderRequest> orderRequests) {
		for (OrderRequest orderRequest : orderRequests) {
			Book book = bookService.getBook(orderRequest.getBookId());
			if(orderRequest.getQuantity() < 1 || orderRequest.getQuantity() > 100000) {
				throw new ValidationException("invalid quantity");
			}
			if (book.getInventory().getQuantity() < orderRequest.getQuantity()) {
				throw new InsufficientStockException("Insufficient stock available for book : " + book.getName());
			}
		}
		return true;
	}

	private Order createOrder(List<OrderRequest> orderRequests, Customer customer) {
		Order order = new Order();
		Double amount = 0.0;
		Integer itemCount = 0;
		List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();
		for (OrderRequest orderRequest : orderRequests) {
			Book book = bookService.getBook(orderRequest.getBookId());
			OrderDetail orderDetail = new OrderDetail(book, orderRequest.getQuantity());
			orderDetails.add(orderDetail);
			amount+=book.getPrice()*orderRequest.getQuantity();
			itemCount+=orderRequest.getQuantity();
		}
		order.setStatus(OrderStatus.PENDING);
		order.setOrderDetails(orderDetails);
		order.setCustomer(customer);
		order.setDate(LocalDateTime.now());
		order.setAmount(amount);
		order.setItemCount(itemCount);
		order = orderRepo.save(order);
		return order;
	}
	
	private void updateInventory(Order order) {
		for (OrderDetail orderDetail : order.getOrderDetails()) {
			Book book = orderDetail.getBook();
			Integer quantity = book.getInventory().getQuantity() - orderDetail.getQuantity();
			book.getInventory().setQuantity(quantity);
			bookService.saveBook(book);
		}
	}

	public Order getOrder(UUID id) {
		return orderRepo.findById(id).orElseThrow(OrderNotFoundException::new);
	}

	public Page<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate , Pageable pageable) {
		return orderRepo.findByDateBetween(startDate, endDate, pageable);
	}
	
	public Page<Order> getPagedCustomerOrderDetails(Customer customer, Pageable pageable) {
		return orderRepo.findByCustomer(customer, pageable);
	}

}
