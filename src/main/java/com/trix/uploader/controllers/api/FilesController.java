package com.trix.uploader.controllers.api;

import com.trix.uploader.model.FileModel;
import com.trix.uploader.services.FileService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FilesController {

    private FileService fileService;

    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("getFiles")
    public Map<String, Object> getFilesUnderPath(@RequestParam("path") String path) {
        Map<String, Object> response = new HashMap<>();
        response.put("files", fileService.getFilesUnderPath(path));
        response.put("path", path);
        return response;
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadFiles(@RequestPart MultipartFile[] files, @RequestPart(required = false) String path) {

        List<FileModel> savedFiles = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();

        if (path == null) {
            path = "";
        }

        for (MultipartFile file : files) {
            savedFiles.add(fileService.save(file, path.split("/")));
        }

        response.put("files", savedFiles);

        return response;
    }

    @PostMapping(value = "/create_directory")
    public Map<String, Object> createDirectory(@RequestPart(required = false) String path, @RequestPart String directoryName) {
        Map<String, Object> response = new HashMap<>();

        if (path == null) {
            path = "";
        }

        FileModel directory = fileService.createDirectory(path, directoryName);

        response.put("directory", directory);

        return response;
    }
}
