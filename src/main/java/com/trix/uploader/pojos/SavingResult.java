package com.trix.uploader.pojos;

import com.trix.uploader.model.FileModel;
import lombok.Getter;

@Getter
public class SavingResult {

    private final FileModel fileModel;

    private final Boolean isSaved;

    public SavingResult(FileModel fileModel) {
        this.fileModel = fileModel;
        this.isSaved = false;
    }

    public SavingResult(FileModel fileModel, Boolean isSaved) {
        this.fileModel = fileModel;
        this.isSaved = isSaved;
    }
}
