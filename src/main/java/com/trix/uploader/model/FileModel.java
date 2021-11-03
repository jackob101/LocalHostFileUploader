package com.trix.uploader.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public final class FileModel {

    private final String name;
    private final String path;
    private final boolean isDirectory;
    private final LocalDateTime modifiedDate;

    public FileModel(String fileName, String path) {
        this.name = fileName;
        this.path = path;
        this.isDirectory = false;
        this.modifiedDate = LocalDateTime.now();
    }

    public FileModel(String fileName, String path, boolean isDirectory) {
        this.name = fileName;
        this.path = path;
        this.isDirectory = isDirectory;
        this.modifiedDate = LocalDateTime.now();
    }

    public FileModel(String name, String path, boolean isDirectory, LocalDateTime modifiedDate) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.modifiedDate = modifiedDate;
    }
}
