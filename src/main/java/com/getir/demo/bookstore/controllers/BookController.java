package com.getir.demo.bookstore.controllers;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.getir.demo.bookstore.models.Admin;
import com.getir.demo.bookstore.models.Book;
import com.getir.demo.bookstore.models.BookInventoryUpdateRequest;
import com.getir.demo.bookstore.services.AdminService;
import com.getir.demo.bookstore.services.BookService;

@RestController
@RequestMapping("/books")
public class BookController {
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private AdminService adminService;

	@PostMapping("/registration")
	public ResponseEntity<Book> saveBook(@RequestBody @Valid Book book,@NotNull @RequestHeader(name="Authorization") UUID id) {
		Admin admin = adminService.authenticate(id);
		return ResponseEntity.status(HttpStatus.CREATED).body(bookService.saveBook(book)); 
	}
	
	@PutMapping("/inventory")
	public ResponseEntity<Void> updateInventory(@RequestBody @Valid BookInventoryUpdateRequest request,@NotNull @RequestHeader(name="Authorization") UUID id){
		Admin admin = adminService.authenticate(id);
		bookService.updateInventory(request.getBookId(), request.getQuantity());
		return ResponseEntity.noContent().build();
	}
	
}
