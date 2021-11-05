package com.trix.uploader.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileModel fileModel = (FileModel) o;
        return isDirectory == fileModel.isDirectory && name.equals(fileModel.name) && path.equals(fileModel.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, isDirectory);
    }
}
