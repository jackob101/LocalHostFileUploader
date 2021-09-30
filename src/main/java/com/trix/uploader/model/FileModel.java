package com.trix.uploader.model;

import lombok.Getter;

@Getter
public class FileModel {

    private final String fileName;
    private final boolean isDirectory;

    public FileModel(String fileName) {
        this.fileName = fileName;
        this.isDirectory = false;
    }

    public FileModel(String fileName, boolean isDirectory) {
        this.fileName = fileName;
        this.isDirectory = isDirectory;
    }
}
