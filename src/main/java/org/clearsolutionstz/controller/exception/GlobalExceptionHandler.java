package org.clearsolutionstz.controller.exception;

import org.clearsolutionstz.service.exception.UserDataRestrictionException;
import org.clearsolutionstz.service.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Map<String, List<String>>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, List<String>> result = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> {
                    if (result.containsKey(e.getField())) {
                        result.get(e.getField()).add(e.getDefaultMessage());
                    } else {
                        result.put(e.getField(), Collections.singletonList(e.getDefaultMessage()));
                    }
                });
        return new ResponseEntity<>(getErrorsMap(result), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Map<String, List<String>>> userNotFoundException(UserNotFoundException ex) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("errors", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(map, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {UserDataRestrictionException.class})
    public ResponseEntity<Map<String, List<String>>> userDataRestrictionException(UserDataRestrictionException ex) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("errors", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(map, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, Map<String, List<String>>> getErrorsMap(Map<String, List<String>> errors) {
        Map<String, Map<String, List<String>>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
