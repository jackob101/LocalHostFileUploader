package com.trix.uploader.services;

import com.trix.uploader.exceptions.EmptyFileException;
import com.trix.uploader.model.FileModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

@Service
public class FileService {

    private final Path uploadDirectory;

    public FileService(@Value("${upload.location}") String filesPath) {
        String[] arrayPath = stream(filesPath.split("/"))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        //TODO add dynamic way to get root path
        this.uploadDirectory = Paths.get("/", arrayPath);
    }

    public boolean save(MultipartFile file, String[] path) {
        return save(file, Path.of("", path));
    }

    public boolean save(MultipartFile file, Path requestUploadPath) {

        if (file == null || file.isEmpty())
            throw new EmptyFileException();

        String fileName = StringUtils.cleanPath(requireNonNull(file.getOriginalFilename()));
        Path finalPath = this.uploadDirectory.resolve(requestUploadPath);

        try {
            Files.copy(file.getInputStream(), finalPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public List<FileModel> getFilesUnderPath(String path) {

        ArrayList<FileModel> files = new ArrayList<>();
        String[] arrayPath;

        if (path.isEmpty()) {
            arrayPath = new String[0];
        } else {
            arrayPath = stream(path.split("/"))
                    .filter(entry -> !entry.isEmpty())
                    .toArray(String[]::new);
        }

        File file = new File(uploadDirectory.resolve(Paths.get("", arrayPath)).toUri());

        for (File entry : requireNonNull(file.listFiles())) {

            if (entry.isDirectory()) {
                files.add(new FileModel(entry.getName(), path, true));
            } else {
                files.add(new FileModel(entry.getName(), path));
            }

        }

        return files;
    }

}
