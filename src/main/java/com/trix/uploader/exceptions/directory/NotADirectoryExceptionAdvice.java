package com.trix.uploader.exceptions.directory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class NotADirectoryExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(value = NotADirectoryException.class)
    public ResponseEntity<Object> handleException(NotADirectoryException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
