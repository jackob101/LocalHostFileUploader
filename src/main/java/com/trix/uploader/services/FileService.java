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

    public List<FileModel> getAllFileNames() {

        return readDirectory(filesPath);
    }

    private List<FileModel> readDirectory(String path) {

        List<FileModel> fileModelList = new ArrayList<>();

        File file = new File(path);

        for (File entry : Objects.requireNonNull(file.listFiles())) {

            if (entry.isDirectory()) {
                List<FileModel> subFiles = readDirectory(entry.getPath());
                fileModelList.add(new FileModel(entry.getName(), true, subFiles));
            } else {
                fileModelList.add(new FileModel(entry.getName()));
            }

        }

        return fileModelList;
    }
}
