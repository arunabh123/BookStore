package com.getir.demo.bookstore.models;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerRegistrationResponse {
	private UUID userToken;
}
