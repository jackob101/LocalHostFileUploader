package com.trix.uploader.exceptions.advices;

import org.apache.tomcat.util.http.fileupload.impl.SizeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class SizeLimitExceededExceptionAdvice {

    @ExceptionHandler(value = SizeException.class)
    @ResponseBody
    public ResponseEntity<Object> handleException(SizeException ex) {

        return new ResponseEntity<>("Size of uploaded files is too big", HttpStatus.BAD_REQUEST);

    }
}
