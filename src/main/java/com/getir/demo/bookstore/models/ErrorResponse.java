package com.getir.demo.bookstore.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

	private String code;
    private String message;
    private List<String> details = new ArrayList<String>();
    private LocalDateTime timestamp;
}
