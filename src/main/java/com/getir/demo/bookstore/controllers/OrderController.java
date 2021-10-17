package com.getir.demo.bookstore.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.getir.demo.bookstore.models.Admin;
import com.getir.demo.bookstore.models.Customer;
import com.getir.demo.bookstore.models.Order;
import com.getir.demo.bookstore.models.OrderRequest;
import com.getir.demo.bookstore.models.OrderResponse;
import com.getir.demo.bookstore.models.OrderSearchRequest;
import com.getir.demo.bookstore.models.PagedOrdersResponse;
import com.getir.demo.bookstore.models.Views;
import com.getir.demo.bookstore.services.AdminService;
import com.getir.demo.bookstore.services.CustomerService;
import com.getir.demo.bookstore.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private AdminService adminService;

	@PostMapping("/create_new")
	public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody List<OrderRequest> orderRequests,
			@NotNull @RequestHeader(name = "Authorization") UUID customerId) {
		Customer customer = customerService.authenticate(customerId);
		Order order = orderService.placeOrder(orderRequests, customer);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new OrderResponse(order.getId(), order.getDate(), order.getStatus()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrder(@PathVariable("id") @NotNull UUID id,
			@NotNull @RequestHeader(name = "Authorization") UUID adminId) {
		Admin admin = adminService.authenticate(adminId);
		return ResponseEntity.ok(orderService.getOrder(id));
	}

	@JsonView(Views.OrderComplete.class)
	@PostMapping("/search")
	public ResponseEntity<PagedOrdersResponse> getOrdersByDate(@Valid @RequestBody OrderSearchRequest searchRequest,
			@NotNull @RequestHeader(name = "Authorization") UUID adminId, @RequestParam(name="size",defaultValue="10") int size, @RequestParam(name="page",defaultValue="1")  int page) {
		Admin admin = adminService.authenticate(adminId);
		Pageable paging = PageRequest.of(page-1, size);
		Page<Order> pagedOrders = orderService.getOrdersByDateRange(searchRequest.getStartDate(),
				searchRequest.getEndDate(), paging);
		return ResponseEntity.ok(new PagedOrdersResponse(pagedOrders.getContent(), pagedOrders.getNumber(), pagedOrders.getTotalPages(), pagedOrders.getTotalElements()));
	}
}
