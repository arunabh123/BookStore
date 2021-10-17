package com.getir.demo.bookstore.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.getir.demo.bookstore.models.Customer;
import com.getir.demo.bookstore.models.MonthlyCustomerOrderStats;
import com.getir.demo.bookstore.models.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order, UUID>{

	Page<Order> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
	
	Order findByIdAndCustomer(UUID id, Customer customer);
	
	Page<Order> findByCustomer(Customer customer, Pageable pageable);
	
	@Aggregation(pipeline = {"{\n" + 
			"            $match: {\n" + 
			"                $and: [ \n" + 
			"                    {\"customer.$id\" : ?0 } ,\n" + 
			"                    { \"status\": \"SUCCESS\" }\n" + 
			"         ]\n" + 
			"                }\n" + 
			"    }"
			,"{\n" + 
			"    $group: {\n" + 
			"        _id: { year: { $year: \"$date\" }, month: { $month: \"$date\" } },\n" + 
			"        total_cost_month: { $sum: \"$amount\" },\n" + 
			"        total_order_month:{ $sum: 1 },\n" + 
			"        total_books_month:{ $sum: \"$itemCount\" }\n" + 
			"        }\n" + 
			"    }", 
			"    {$project: {\n" + 
			"        _id: false,\n" + 
			"        year: '$_id.year',\n" + 
			"        month: '$_id.month',\n" + 
			"        totalAmount: '$total_cost_month',\n" + 
			"        totalOrders: '$total_order_month',\n" + 
			"        totalBooks: '$total_books_month'\n" + 
			"     }}"})		
	AggregationResults<MonthlyCustomerOrderStats> getMonthlyOrderStats(UUID customerId);
}
