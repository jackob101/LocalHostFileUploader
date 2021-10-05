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
import java.util.Objects;

@Service
public class FileService {

    private String filesPath;

    public FileService(@Value("${upload.location}") String filesPath) {
        this.filesPath = filesPath;
    }

    public boolean save(MultipartFile file) {
        if (file.isEmpty())
            throw new EmptyFileException();

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            Path path = Paths.get(filesPath + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<FileModel> getFilesUnderPath(String path) {
        ArrayList<FileModel> files = new ArrayList<>();

        if (path.length() > 0 && path.charAt(0) == '/') {
            path = path.substring(1);
        }

        File file = new File(filesPath + path);

        for (File entry : Objects.requireNonNull(file.listFiles())) {

            if (entry.isDirectory()) {
                files.add(new FileModel(entry.getName(), path, true));
            } else {
                files.add(new FileModel(entry.getName(), path));
            }

        }

        return files;
    }

}
