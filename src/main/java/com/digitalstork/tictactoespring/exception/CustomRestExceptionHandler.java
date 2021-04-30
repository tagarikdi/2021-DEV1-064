package com.digitalstork.tictactoespring.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class CustomRestExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CustomRestExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Error> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(errorMessage(HttpStatus.BAD_REQUEST, ex, request));
    }

    private Error errorMessage(HttpStatus httpStatus, IllegalArgumentException ex, WebRequest request) {
        var error = Error.builder()
                .status(httpStatus)
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .path(((ServletWebRequest) request).getRequest().getRequestURI())
                .build();
        LOG.info("{}", error);
        return error;
    }
}