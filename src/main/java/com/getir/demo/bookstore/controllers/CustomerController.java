package com.getir.demo.bookstore.controllers;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.getir.demo.bookstore.models.Customer;
import com.getir.demo.bookstore.models.CustomerRegistrationResponse;
import com.getir.demo.bookstore.models.Order;
import com.getir.demo.bookstore.models.PagedOrdersResponse;
import com.getir.demo.bookstore.models.Views;
import com.getir.demo.bookstore.services.CustomerService;
import com.getir.demo.bookstore.services.OrderService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/registration")
	public ResponseEntity<CustomerRegistrationResponse> saveCustomer(@RequestBody @Valid Customer customer) {
		UUID id = customerService.saveCustomer(customer).getId(); 
		return ResponseEntity.status(HttpStatus.CREATED).body(new CustomerRegistrationResponse(id));
	}
	
	@JsonView(Views.OrderBasic.class)
	@GetMapping("/orders")
	public ResponseEntity<PagedOrdersResponse> getOrders(@NotNull @RequestHeader(name="Authorization") UUID id , @RequestParam(name="size",defaultValue="10") int size, @RequestParam(name="page",defaultValue="1")  int page) {
		Customer customer = customerService.authenticate(id);
		Pageable paging = PageRequest.of(page-1, size);
		Page<Order> pagedOrders = orderService.getPagedCustomerOrderDetails(customer, paging);
		return ResponseEntity.ok(new PagedOrdersResponse(pagedOrders.getContent(), pagedOrders.getNumber(), pagedOrders.getTotalPages(), pagedOrders.getTotalElements()));
	}
}
