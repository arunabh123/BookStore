package com.getir.demo.bookstore.repositories;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.getir.demo.bookstore.models.Customer;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, UUID> {

	@Query("{ email : '?0' }")
	Customer findByEmail(String email);

}
