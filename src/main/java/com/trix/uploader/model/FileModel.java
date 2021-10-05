package com.trix.uploader.model;

import lombok.Getter;

@Getter
public final class FileModel {

    private final String name;
    private final String path;
    private final boolean isDirectory;

    public FileModel(String fileName, String path) {
        this.name = fileName;
        this.path = path;
        this.isDirectory = false;
    }

    public FileModel(String fileName, String path, boolean isDirectory) {
        this.name = fileName;
        this.path = path;
        this.isDirectory = isDirectory;
    }
}
