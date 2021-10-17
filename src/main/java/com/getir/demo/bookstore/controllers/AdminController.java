package com.getir.demo.bookstore.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.getir.demo.bookstore.models.Admin;
import com.getir.demo.bookstore.services.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@PostMapping("/registration")
	public ResponseEntity<Admin> createAdmin(@Valid @RequestBody Admin adminUser) {
		return ResponseEntity.status(HttpStatus.CREATED).body(adminService.saveAdmin(adminUser));
	}
}
