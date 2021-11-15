package com.trix.uploader.exceptions.advices;

import com.trix.uploader.exceptions.FileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
public class FileExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(value = FileException.class)
    public ResponseEntity<Object> handleException(FileException ex) {
        List<String> message = List.of(ex.getMessage());

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}
