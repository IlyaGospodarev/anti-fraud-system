package antifraud.controller;

import antifraud.exception.BadRequestException;
import antifraud.exception.HttpConflictException;
import antifraud.exception.UnprocessableEntityException;
import antifraud.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    protected ResponseEntity<Object> handleException(BadRequestException ex) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleException(HttpConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    @ExceptionHandler
    protected ResponseEntity<Object> handleException(NotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleException(UnprocessableEntityException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    @ExceptionHandler
    protected ResponseEntity<Object>  handleException(Exception ex) {
        return ResponseEntity.badRequest().build();
    }
}
