package com.getir.demo.bookstore.models;

import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookInventoryUpdateRequest {

	@NotNull
	private UUID bookId;
	
	@Min(value=0)
	@Max(value=1000000)
	private Integer quantity;
}
