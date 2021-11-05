package com.trix.uploader.exceptions.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class FileNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(value = FileNotFoundException.class)
    public ResponseEntity<Object> handleException(FileNotFoundException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
