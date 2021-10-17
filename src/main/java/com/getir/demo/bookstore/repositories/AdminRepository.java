package com.getir.demo.bookstore.repositories;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.getir.demo.bookstore.models.Admin;

@Repository
public interface AdminRepository extends MongoRepository<Admin, UUID>{

	Admin findByName(String name);
}
