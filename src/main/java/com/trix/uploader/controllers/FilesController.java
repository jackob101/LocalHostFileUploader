package com.trix.uploader.controllers;

import com.trix.uploader.model.FileModel;
import com.trix.uploader.services.FileService;
import com.trix.uploader.validators.PathValidator;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FilesController {

    private final FileService fileService;
    private final PathValidator pathValidator;

    public FilesController(FileService fileService) {
        this.fileService = fileService;
        this.pathValidator = new PathValidator();
    }

    @GetMapping("get_files")
    public ResponseEntity<Object> getFilesUnderPath(@RequestParam("path") String path) throws FileNotFoundException {

        pathValidator.validate(path);

        Map<String, Object> response = new HashMap<>();
        response.put("files", fileService.getFilesUnderPath(path));
        response.put("path", path);

        return ResponseEntity.ok(response);
    }

    @GetMapping("get_file_content")
    public ResponseEntity<String> getFileContent(@RequestParam(value = "path") String path) throws FileNotFoundException {

        pathValidator.validate(path);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileService.getFile(path))));
        String response = bufferedReader.lines().collect(Collectors.joining());

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFiles(@RequestPart MultipartFile[] files,
                                              @RequestParam(required = false, defaultValue = "") String path,
                                              @RequestParam(required = false, defaultValue = "false") Boolean override) {

        Map<String, Object> response = new HashMap<>();

        pathValidator.validate(path);

        Map<String, List<FileModel>> savedFiles = fileService.saveAll(
                Paths.get(path),
                Arrays.asList(files),
                override
        );

        response.put("files", savedFiles);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping(value = "create_directory")
    public ResponseEntity<Object> createDirectory(@RequestPart(required = false) String path) {
        Map<String, Object> response = new HashMap<>();

        pathValidator.validate(path);

        if (path == null) {
            path = "";
        }

        FileModel directory = fileService.createDirectory(path);

        response.put("directory", directory);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping(value = "download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("path") String path) throws IOException {

        pathValidator.validate(path);

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
    public ResponseEntity<FileModel> updateFileName(@RequestParam(value = "path", defaultValue = "") String path,
                                                    @RequestParam("oldName") String oldName,
                                                    @RequestParam("newName") String newName) {

        pathValidator.validate(path);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(fileService.updateName(path, oldName, newName));

    }

    @PostMapping(value = "delete")
    public ResponseEntity<Object> delete(@RequestParam(value = "path", defaultValue = "") String path) {

        pathValidator.validate(path);
        HashMap<String, Object> responseBody = new HashMap<>();
        responseBody.put("deleted", fileService.delete(path));

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping(value = "new_note")
    public ResponseEntity<Object> createNewNote(@RequestParam(value = "path", defaultValue = "") String path,
                                                @RequestParam(value = "content") String content,
                                                @RequestParam(value = "name") String name,
                                                @RequestParam(value = "editing") Boolean editing) throws FileAlreadyExistsException {

        pathValidator.validate(path);
        FileModel fileFromNote = fileService.createFileFromNote(path, content, name, editing);

        return ResponseEntity.ok(fileFromNote);
    }
}
