package com.getir.demo.bookstore.models;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("book")
@JsonIgnoreProperties(value = { "target" })
public class Book {

	@Id
	private UUID id;
	
	@NotEmpty
	private String name;
	@NotEmpty
	private String author;
	private Double price;
	@JsonIgnore
	private Inventory inventory;
	
	public Book() {
		setId(UUID.randomUUID());
		setInventory(new Inventory());
	}
	
	
}
