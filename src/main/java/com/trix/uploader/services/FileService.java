package com.trix.uploader.services;

import com.trix.uploader.exceptions.EmptyFileException;
import com.trix.uploader.exceptions.path.AbsolutePathException;
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


    public List<FileModel> saveAll(List<MultipartFile> files, Path uploadRequestPath) {

        if (uploadRequestPath.isAbsolute())
            throw new AbsolutePathException();

        return files.stream().map(file -> save(file, uploadRequestPath)).filter(Objects::nonNull).toList();
    }


    public FileModel save(MultipartFile file, Path uploadRequestPath) {

        if (file == null || file.isEmpty())
            throw new EmptyFileException();

        String fileName = StringUtils.cleanPath(requireNonNull(file.getOriginalFilename()));
        Path relativePath = uploadRequestPath.resolve(fileName);
        Path absolutePath = uploadDirectory.resolve(relativePath);

        File oldFile = new File(absolutePath.toUri());

        if (!oldFile.exists()) {

            try {
                Files.copy(file.getInputStream(), absolutePath, StandardCopyOption.REPLACE_EXISTING);
                File savedFile = new File(absolutePath.toUri());
                return new FileModel(savedFile.getName(), absolutePath.toString());
            } catch (IOException e) {
                //TODO add some handling here
                e.printStackTrace();
            }

        }
        return null;
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

            files.add(new FileModel(entry.getName(), path, entry.isDirectory()));

        }

        return files;
    }

    public FileModel createDirectory(String path, String directoryName) {

        String normalizedPath = StringUtils.cleanPath(path);
        Path relativePath = Paths.get(normalizedPath);

        if (relativePath.isAbsolute())
            throw new AbsolutePathException();

        Path absolutePath = uploadDirectory.resolve(Paths.get(path).resolve(directoryName));

        File file = new File(absolutePath.toUri());
        FileModel fileModel = null;

        if (file.exists() && file.isDirectory()) {
            fileModel = new FileModel(file.getName(), path, true);
        } else {

            try {

                Path directories = Files.createDirectory(absolutePath);
                File createdDirectory = new File(directories.toUri());

                Path newDirRelativePath = uploadDirectory.relativize(directories);
                String pathString = "";

                if (newDirRelativePath.getNameCount() >= 2)
                    pathString = newDirRelativePath.subpath(0, newDirRelativePath.getNameCount() - 1).toString();

                fileModel = new FileModel(directoryName, pathString, true);
            } catch (IOException e) {
                //TODO Do some handling on this exception
                e.printStackTrace();
            }
        }

        return fileModel;

    }
}
