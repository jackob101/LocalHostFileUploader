package com.trix.uploader.exceptions.path;

public class AbsolutePathException extends RuntimeException {

    public AbsolutePathException() {
        super("Path to file should not be absolute");
    }
}
