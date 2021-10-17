package com.getir.demo.bookstore.models;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlyCustomerOrderStats {

	private Integer year;
	@JsonIgnore
	private Integer month;
	private Double totalAmount;
	private Integer totalOrders;
	private Integer totalBooks;
	@JsonProperty("month")
	private String monthName;
	
	public String getMonthName() {
		return Month.of(this.getMonth()).getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
	}
}
