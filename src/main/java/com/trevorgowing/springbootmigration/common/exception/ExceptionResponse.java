package com.trevorgowing.springbootmigration.common.exception;

import static lombok.AccessLevel.PRIVATE;

import java.io.Serializable;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@RequiredArgsConstructor(access = PRIVATE)
public class ExceptionResponse implements Serializable {

  private static final long serialVersionUID = 5795604402500068825L;

  private final int status;
  private final String error;
  private final String message;

  public static ExceptionResponse from(HttpStatus status, String message) {
    return new ExceptionResponse(status.value(), status.getReasonPhrase(), message);
  }
}
