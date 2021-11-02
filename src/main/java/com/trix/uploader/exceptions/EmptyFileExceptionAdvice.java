package com.trix.uploader.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;

@ControllerAdvice
public class EmptyFileExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(EmptyFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> emptyFileHandler(EmptyFileException ex) {
        //TODO add more information to response entity
        return new ResponseEntity<Object>(Collections.singleton(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
