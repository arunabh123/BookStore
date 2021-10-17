package com.getir.demo.bookstore.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.getir.demo.bookstore.exceptions.BookNotFoundException;
import com.getir.demo.bookstore.models.Book;
import com.getir.demo.bookstore.repositories.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepo;
	
	public Book saveBook(Book book) {
		return bookRepo.save(book);
	}
	
	public void updateInventory(UUID id, Integer quantity) {
		Book book = bookRepo.findById(id).orElseThrow(BookNotFoundException::new);
		book.getInventory().setQuantity(quantity);
		bookRepo.save(book);
	}
	
	public Book getBook(UUID id) {
		return bookRepo.findById(id).orElseThrow(BookNotFoundException::new);
	}
}
