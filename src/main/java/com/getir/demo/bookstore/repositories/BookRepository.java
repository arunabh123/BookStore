package com.getir.demo.bookstore.repositories;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.getir.demo.bookstore.models.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, UUID> {

	Book findBookByName(String name);
}
