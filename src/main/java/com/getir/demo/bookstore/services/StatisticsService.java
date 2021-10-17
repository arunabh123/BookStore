package com.getir.demo.bookstore.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.getir.demo.bookstore.models.Customer;
import com.getir.demo.bookstore.models.MonthlyCustomerOrderStats;
import com.getir.demo.bookstore.repositories.OrderRepository;

@Service
public class StatisticsService {

	@Autowired
	private OrderRepository orderRepo;

	public List<MonthlyCustomerOrderStats> getMonthlyOrderStats(Customer customer) {
		AggregationResults<MonthlyCustomerOrderStats> stats = orderRepo.getMonthlyOrderStats(customer.getId());
		return stats.getMappedResults();
	}
	
}
