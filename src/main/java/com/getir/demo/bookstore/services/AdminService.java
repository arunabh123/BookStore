package com.getir.demo.bookstore.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.getir.demo.bookstore.exceptions.AdminNotFoundException;
import com.getir.demo.bookstore.exceptions.UnauthorisedAccessException;
import com.getir.demo.bookstore.models.Admin;
import com.getir.demo.bookstore.repositories.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepo;

	public Admin getAdmin(UUID id) {
		return adminRepo.findById(id).orElseThrow(AdminNotFoundException::new);
	}
	
	public Admin saveAdmin(Admin user) {
		return adminRepo.save(user);
	}
	
	public Admin authenticate(UUID id) {
		return adminRepo.findById(id).orElseThrow(UnauthorisedAccessException::new);
	}
}
