package com.trix.uploader.controllers.api;

import com.trix.uploader.services.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
