package com.trix.uploader.exceptions.path;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class PathExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(value = PathException.class)
    public ResponseEntity<Object> absolutePathHandler(PathException ex) {

        List<String> errors = Collections.singletonList(ex.getMessage());
        //TODO add some more info to response
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
