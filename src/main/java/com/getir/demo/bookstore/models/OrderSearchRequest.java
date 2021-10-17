package com.getir.demo.bookstore.models;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchRequest {

	@NotNull
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate startDate;
	
	@NotNull
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate endDate;
}
