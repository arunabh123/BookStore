package com.getir.demo.bookstore.services;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.getir.demo.bookstore.exceptions.CustomerNotFoundException;
import com.getir.demo.bookstore.exceptions.DuplicateCustomerException;
import com.getir.demo.bookstore.exceptions.UnauthorisedAccessException;
import com.getir.demo.bookstore.models.Customer;
import com.getir.demo.bookstore.repositories.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepo;

	public Customer saveCustomer(Customer customer) {
		if (!this.isExistingCustomer(customer)) {
			return customerRepo.save(customer);
		} else {
			throw new DuplicateCustomerException();
		}

	}
	
	public Customer updateCustomer(Customer customer) {
		if (this.isExistingCustomer(customer)) {
			return customerRepo.save(customer);
		} else {
			throw new CustomerNotFoundException();
		}
	}

	private boolean isExistingCustomer(Customer customer) {
		if (Objects.nonNull(customerRepo.findByEmail(customer.getEmail()))) {
			return true;
		} else {
			return false;
		}
	}

	public Customer getCustomer(UUID id) {
		return customerRepo.findById(id).orElseThrow(CustomerNotFoundException::new);
	}
	
	public Customer authenticate(UUID id) {
		return customerRepo.findById(id).orElseThrow(UnauthorisedAccessException::new);
	}
}
