package com.getir.demo.bookstore.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PagedOrdersResponse {
	private List<Order> orders;
	private Integer currentPage;
	private Integer totalPages;
	private Long totalItems;
}
