package com.trix.uploader.exceptions.directory;

public class NotADirectoryException extends RuntimeException {

    public NotADirectoryException() {
        super("Path does not lead to directory");
    }
}
