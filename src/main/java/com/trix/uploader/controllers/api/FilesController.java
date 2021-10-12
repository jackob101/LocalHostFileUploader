package com.trix.uploader.controllers.api;

import com.trix.uploader.services.FileService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
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

        if (path == null) {
            path = "";
        }

        for (MultipartFile file : files) {
            fileService.save(file, path.split("/"));
        }

        //TODO Temporary implementation
        return new HashMap<>();
    }
}
