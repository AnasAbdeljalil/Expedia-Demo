package com.example.expediasearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import jakarta.validation.ConstraintViolationException;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	//////////////////////////////////////////////////////
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ConstraintViolationException e, WebRequest request) {
		logger.error("Validation error: {}", e.getMessage());

		String message = e.getConstraintViolations().stream().map(violation -> violation.getMessage()).findFirst()
				.orElse("Validation failed");

		ErrorResponse errorResponse = new ErrorResponse(message, request.getDescription(false));
		return new ResponseEntity<>(errorResponse, BAD_REQUEST);
	}

	// (4xx errors)
	//////////////////////////////////////////////////////
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<ErrorResponse> handleClientErrorException(HttpClientErrorException e, WebRequest request) {
		logger.error("Client error occurred: {}", e.getMessage());

		ErrorResponse errorResponse = new ErrorResponse("Client error: " + e.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorResponse, e.getStatusCode());
	}

	// (5xx errors)
	//////////////////////////////////////////////////////
	@ExceptionHandler(HttpServerErrorException.class)
	public ResponseEntity<ErrorResponse> handleServerErrorException(HttpServerErrorException e, WebRequest request) {
		logger.error("Server error occurred: {}", e.getMessage());

		ErrorResponse errorResponse = new ErrorResponse("Server error: " + e.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorResponse, e.getStatusCode());
	}

	//////////////////////////////////////////////////////
	@ExceptionHandler(RestClientException.class)
	public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException e, WebRequest request) {
		logger.error("Network error occurred: {}", e.getMessage());

		ErrorResponse errorResponse = new ErrorResponse("Service unavailable: " + e.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorResponse, SERVICE_UNAVAILABLE);
	}

	//////////////////////////////////////////////////////
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
			MethodArgumentTypeMismatchException ex, WebRequest request) {
		logger.error("Invalid parameter type: {}", ex.getMessage());

		ErrorResponse errorResponse = new ErrorResponse("Invalid parameter: " + ex.getName(),
				request.getDescription(false));
		return new ResponseEntity<>(errorResponse, BAD_REQUEST);
	}

	//////////////////////////////////////////////////////
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
		logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);

		ErrorResponse errorResponse = new ErrorResponse("Internal server error: " + ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorResponse, INTERNAL_SERVER_ERROR);
	}
}