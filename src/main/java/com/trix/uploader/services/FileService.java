package com.trix.uploader.services;

import com.trix.uploader.exceptions.EmptyFileException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    private final String FILES_PATH = "./uploadContent/";

    public boolean save(MultipartFile file) {
        if (file.isEmpty())
            throw new EmptyFileException();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Path path = Paths.get(FILES_PATH + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
