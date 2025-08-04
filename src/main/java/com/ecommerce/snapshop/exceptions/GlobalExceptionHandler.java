package com.ecommerce.snapshop.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@NoArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> methodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error->{
            String fieldName=((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> resourceNotFoundException(ResourceNotFoundException e){
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<String> apiException(APIException e){
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> apiException(CategoryNotFoundException e){
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);

    }
}
