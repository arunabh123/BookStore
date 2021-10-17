package com.getir.demo.bookstore.exceptions;

public class InsufficientStockException extends RuntimeException {

	public InsufficientStockException(String string) {
		super(string);
	}

}
