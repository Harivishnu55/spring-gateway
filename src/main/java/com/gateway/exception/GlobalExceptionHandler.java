package com.gateway.exception;

import com.gateway.constants.AppConstants;
import com.gateway.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                    .success(Boolean.FALSE)
                    .message(ex.getMessage())
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .timeStamp(LocalDateTime.now().toString())
                    .build());
    }

    @ExceptionHandler(UnAuthorizedRequestException.class)
    public ResponseEntity<ErrorResponse> handleUnAuthorizedRequest(UnAuthorizedRequestException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                .success(Boolean.FALSE)
                .message(ex.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .timeStamp( LocalDateTime.now().format(DateTimeFormatter.ofPattern(AppConstants.RESPONSE_DATE)))
                .build());
    }
}
