package com.ecommerce.snapshop.exceptions;

import com.ecommerce.snapshop.config.AppConstants;
import jakarta.validation.ConstraintViolationException;
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
    public ResponseEntity<Map<String,APIResponse>> methodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String,APIResponse> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error->{
            String fieldName=((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            APIResponse apiResponse = new APIResponse(errorMessage,AppConstants.FALSE);
            errors.put(fieldName,apiResponse);
        });
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> resourceNotFoundException(ResourceNotFoundException e){
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message,AppConstants.FALSE);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> apiException(APIException e){
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, AppConstants.FALSE);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<APIResponse> apiException(CategoryNotFoundException e){
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message,AppConstants.FALSE);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,APIResponse>> constraintVioationException(ConstraintViolationException ex){
        Map<String,APIResponse> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(error->{
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            APIResponse apiResponse = new APIResponse(errorMessage,AppConstants.FALSE);
            errors.put(fieldName,apiResponse);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
