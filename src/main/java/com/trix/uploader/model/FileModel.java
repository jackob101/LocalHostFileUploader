package com.trix.uploader.model;

import lombok.Getter;

import java.util.List;

@Getter
public final class FileModel {

    private final String fileName;
    private final boolean isDirectory;
    private final List<FileModel> subFiles;

    public FileModel(String fileName) {
        this.fileName = fileName;
        this.isDirectory = false;
        this.subFiles = null;
    }

    public FileModel(String fileName, boolean isDirectory, List<FileModel> subFiles) {
        this.fileName = fileName;
        this.isDirectory = isDirectory;
        this.subFiles = subFiles;
    }
}
