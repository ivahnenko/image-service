package com.example.demo.error;

public class AuthorizationFailedException extends RuntimeException {

  public AuthorizationFailedException(final String errorMessage) {
    super(errorMessage);
  }

}
