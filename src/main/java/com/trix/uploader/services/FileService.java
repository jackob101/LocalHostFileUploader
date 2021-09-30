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

@Service
public class FileService {

    @Value("${upload.location}")
    private String filesPath;

    public boolean save(MultipartFile file) {
        if (file.isEmpty())
            throw new EmptyFileException();

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

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
        File file = new File(filesPath);

        List<FileModel> listOfFiles = new ArrayList<>();
        for (File entry : file.listFiles()) {
            listOfFiles.add(new FileModel(entry.getName(), entry.isDirectory()));
        }

        return listOfFiles;


    }
}
