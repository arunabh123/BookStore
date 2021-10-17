package com.getir.demo.bookstore.models;

import java.util.UUID;

import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("admin")
public class Admin {

	@Id
	private UUID id;
	@NotEmpty
	private String name;

	public Admin() {
		setId(UUID.randomUUID());
	}

}
