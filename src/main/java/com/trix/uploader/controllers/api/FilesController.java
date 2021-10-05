package com.trix.uploader.controllers.api;

import com.trix.uploader.model.FileModel;
import com.trix.uploader.services.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FilesController {

    private FileService fileService;

    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("getFiles")
    public List<FileModel> getFilesUnderPath(@RequestParam("path") String path) {
        return fileService.getFilesUnderPath(path);
    }
}
