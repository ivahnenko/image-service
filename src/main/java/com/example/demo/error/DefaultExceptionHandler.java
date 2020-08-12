package com.example.demo.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class DefaultExceptionHandler {

  private static final String NOT_FOUND_MESSAGE = "Not Found";
  private static final String INTERNAL_ERROR_MESSAGE = "Unexpected error";

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity handleNotFound(final NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND_MESSAGE);
  }

  @ExceptionHandler(HttpClientErrorException.class)
  public ResponseEntity handleNotFound(final HttpClientErrorException ex) {
    if (ex.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(NOT_FOUND_MESSAGE);
    }

    return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity exception() {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(INTERNAL_ERROR_MESSAGE);
  }

}
