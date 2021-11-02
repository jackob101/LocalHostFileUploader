package com.trix.uploader.pojos;

import com.trix.uploader.model.FileModel;
import lombok.Getter;

@Getter
public class FileSaved {

    private final FileModel fileModel;

    private final Boolean isSaved;

    public FileSaved(FileModel fileModel) {
        this.fileModel = fileModel;
        this.isSaved = false;
    }

    public FileSaved(FileModel fileModel, Boolean isSaved) {
        this.fileModel = fileModel;
        this.isSaved = isSaved;
    }
}
