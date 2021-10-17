package com.getir.demo.bookstore.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.getir.demo.bookstore.exceptions.BookNotFoundException;
import com.getir.demo.bookstore.exceptions.DuplicateCustomerException;
import com.getir.demo.bookstore.exceptions.InsufficientStockException;
import com.getir.demo.bookstore.exceptions.OrderNotFoundException;
import com.getir.demo.bookstore.exceptions.UnauthorisedAccessException;
//import com.getir.demo.bookstore.models.ErrorResponse;
import com.getir.demo.bookstore.models.ErrorResponse;

@ControllerAdvice
public class ExceptionMapper extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UnauthorisedAccessException.class)
	public ResponseEntity<Object> handleUnauthorisedAccessException(UnauthorisedAccessException e,
			WebRequest request) {
		ErrorResponse error = new ErrorResponse();
		error.setCode("E1000");
		error.setMessage("Unauthorised access");
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(error,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(BookNotFoundException.class)
	public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException e,
			WebRequest request) {
		ErrorResponse error = new ErrorResponse();
		error.setCode("E2000");
		error.setMessage("Book not found");
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DuplicateCustomerException.class)
	public ResponseEntity<Object> handleDuplicateCustomerException(DuplicateCustomerException e,
			WebRequest request) {
		ErrorResponse error = new ErrorResponse();
		error.setCode("E2000");
		error.setMessage("Customer already exists");
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(error,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InsufficientStockException.class)
	public ResponseEntity<Object> handleInsufficientStockException(InsufficientStockException e,
			WebRequest request) {
		ErrorResponse error = new ErrorResponse();
		error.setCode("E3000");
		error.setMessage("Out Of Stock : " + e.getMessage());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<Object> handleOrderNotFoundException(OrderNotFoundException e,
			WebRequest request) {
		ErrorResponse error = new ErrorResponse();
		error.setCode("E3000");
		error.setMessage("Order not found.");
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
	}
	
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
        	if(error instanceof FieldError) {
        		details.add(((FieldError)error).getField()+" : "+error.getDefaultMessage());
        	} else {
        		details.add(error.getDefaultMessage());
        	}
        }
        ErrorResponse error = new ErrorResponse("E4000","Validation Failed", details,LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<Object> handleValidationException(ValidationException e,
			WebRequest request) {
		ErrorResponse error = new ErrorResponse();
		error.setCode("E4001");
		error.setMessage(e.getMessage());
		error.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
	}
	
}
