package com.getir.demo.bookstore.controllers;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.getir.demo.bookstore.models.Customer;
import com.getir.demo.bookstore.models.MonthlyCustomerOrderStats;
import com.getir.demo.bookstore.services.CustomerService;
import com.getir.demo.bookstore.services.StatisticsService;

@RestController
@RequestMapping("/stats")
public class StatisticsController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private StatisticsService statisticsService;
	
	@GetMapping("/orders/monthly")
	public ResponseEntity<List<MonthlyCustomerOrderStats>> getCustomerOrderStats(@NotNull @RequestHeader(name = "Authorization") UUID customerId) {
		Customer customer = customerService.authenticate(customerId);
		List<MonthlyCustomerOrderStats> stats = statisticsService.getMonthlyOrderStats(customer);
		return ResponseEntity.ok(stats);
	}
}
