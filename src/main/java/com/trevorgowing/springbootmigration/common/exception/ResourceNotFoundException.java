package com.trevorgowing.springbootmigration.common.exception;

public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -876261188125866470L;

  private ResourceNotFoundException(String message) {
    super(message);
  }

  public static ResourceNotFoundException causedBy(String message) {
    return new ResourceNotFoundException(message);
  }
}
