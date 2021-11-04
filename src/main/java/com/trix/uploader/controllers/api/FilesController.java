package com.trix.uploader.controllers.api;

import com.trix.uploader.model.FileModel;
import com.trix.uploader.services.FileService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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


    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, Object> uploadFiles(@RequestPart MultipartFile[] files,
                                           @RequestPart Optional<String> path,
                                           @RequestParam(required = false, defaultValue = "false") Boolean override) {

        Map<String, Object> response = new HashMap<>();

        Map<String, List<FileModel>> savedFiles = fileService.saveAll(
                Arrays.asList(files),
                Paths.get(path.orElse("")),
                override
        );

        response.put("files", savedFiles);

        return response;
    }

    @PostMapping(value = "create_directory")
    public Map<String, Object> createDirectory(@RequestPart(required = false) String path, @RequestPart String directoryName) {
        Map<String, Object> response = new HashMap<>();

        if (path == null) {
            path = "";
        }

        FileModel directory = fileService.createDirectory(path, directoryName);

        response.put("directory", directory);

        return response;
    }


    @GetMapping(value = "download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String path) throws IOException {

        File file = fileService.getFile(path);
        ByteArrayResource byteArrayFile = new ByteArrayResource(Files.readAllBytes(file.toPath()));


        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .header("x-suggested-filename", file.getName())
                .body(byteArrayFile);
    }

    @PostMapping(value = "update")
    public FileModel updateFileName(@RequestParam(value = "path", defaultValue = "") String path,
                                    @RequestParam("oldName") String oldName,
                                    @RequestParam("newName") String newName) {

        return fileService.updateName(path, oldName, newName);

    }
}
