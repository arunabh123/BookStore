package com.getir.demo.bookstore.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("order")
public class Order {

	@Id
	private UUID id;
	
	@JsonView(Views.OrderComplete.class)
	@DBRef(lazy = true)
	private Customer customer;

	private List<OrderDetail> orderDetails;
	
	private LocalDateTime date; 
	private Double amount;
	private Integer itemCount;
	private OrderStatus status;

	public Order() {
		setId(UUID.randomUUID());
	}

}
