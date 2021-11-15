package com.trix.uploader.exceptions.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@ControllerAdvice
public class IOExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<Object> handleException(IOException ex) {
        List<String> message = List.of(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
