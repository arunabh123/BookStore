package com.getir.demo.bookstore.models;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {

	private UUID orderId;
	private LocalDateTime date;
	private OrderStatus status;
}
