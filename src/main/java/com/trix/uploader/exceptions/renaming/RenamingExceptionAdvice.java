package com.trix.uploader.exceptions.renaming;

import com.trix.uploader.exceptions.path.AbsolutePathException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class RenamingExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(value = AbsolutePathException.class)
    public ResponseEntity<Object> handleRenamingException(RenamingException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
