package com.getir.demo.bookstore.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.getir.demo.bookstore.models.Customer;
import com.getir.demo.bookstore.models.MonthlyCustomerOrderStats;
import com.getir.demo.bookstore.repositories.OrderRepository;

@Service
@CacheConfig(cacheNames = { "stats" })
public class StatisticsService {

	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private CacheManager cacheManager;

	@Cacheable(value = "stats")
	public List<MonthlyCustomerOrderStats> getMonthlyOrderStats(Customer customer) {
		AggregationResults<MonthlyCustomerOrderStats> stats = orderRepo.getMonthlyOrderStats(customer.getId());
		return stats.getMappedResults();
	}

	@Scheduled(fixedRate = 86400000)
	public void evictAllCache() {
		cacheManager.getCache("stats").clear();
	}

}
