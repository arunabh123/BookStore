package com.getir.demo.bookstore.models;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderDetail {

	@DBRef(lazy = true)
	private Book book;
	private Integer quantity;

}
