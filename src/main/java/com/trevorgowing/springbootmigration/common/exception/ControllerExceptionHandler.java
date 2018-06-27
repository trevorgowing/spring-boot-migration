package com.trevorgowing.springbootmigration.common.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(
      ResourceNotFoundException exception) {
    log.debug(exception.getMessage(), exception);
    return ResponseEntity.status(NOT_FOUND)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(NOT_FOUND, exception.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionResponse> handleUnhandledException(Exception exception) {
    log.error(exception.getMessage(), exception);
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(INTERNAL_SERVER_ERROR, exception.getMessage()));
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception exception,
      Object body,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    if (status.is4xxClientError()) {
      log.debug(exception.getMessage(), exception);
    } else {
      log.error(exception.getMessage(), exception);
    }
    return ResponseEntity.status(status)
        .contentType(APPLICATION_JSON_UTF8)
        .body(ExceptionResponse.from(status, exception.getMessage()));
  }
}
