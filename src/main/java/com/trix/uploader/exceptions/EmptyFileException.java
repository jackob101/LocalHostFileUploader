package com.trix.uploader.exceptions;

public class EmptyFileException extends RuntimeException {

    public EmptyFileException() {
        super("Empty file. Please select correct file");
    }

}

