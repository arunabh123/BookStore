package com.getir.demo.bookstore.models;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("customer")
@JsonIgnoreProperties(value = { "target" })
public class Customer {

	@Id
	private UUID id;
	@NotEmpty
	private String name;
	@NotEmpty
	@Indexed(unique = true)
	private String email;

	public Customer() {
		setId(UUID.randomUUID());
	}
}
