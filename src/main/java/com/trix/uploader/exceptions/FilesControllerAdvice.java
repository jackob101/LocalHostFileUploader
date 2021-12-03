package com.trix.uploader.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@ResponseBody
public class FilesControllerAdvice {


    private Map<Object, Object> getResponse(String message, HttpStatus status) {

        HashMap<Object, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("message", message);
        response.put("status", status);

        return response;
    }

    @ExceptionHandler(Throwable.class)
    public <T extends Throwable> ResponseEntity<Object> handleException(T ex) {
        Map<Object, Object> response = getResponse(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
