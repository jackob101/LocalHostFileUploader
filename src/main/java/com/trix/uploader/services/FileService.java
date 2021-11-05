package com.trix.uploader.services;

import com.trix.uploader.exceptions.EmptyFileException;
import com.trix.uploader.exceptions.path.AbsolutePathException;
import com.trix.uploader.exceptions.renaming.RenamingException;
import com.trix.uploader.model.FileModel;
import com.trix.uploader.pojos.SavingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.mapping;

@Cacheable("files")
@Service
public class FileService {

    private final Path uploadDirectory;

    public FileService(@Value("${upload.location}") String filesPath) {
        this.uploadDirectory = Paths.get(filesPath);
    }


    @CacheEvict(cacheNames = "files", key = "#uploadRequestPath.toString()")
    public Map<String, List<FileModel>> saveAll(List<MultipartFile> files, Path uploadRequestPath, Boolean override) {

        if (uploadRequestPath.isAbsolute())
            throw new AbsolutePathException();

        return files.stream()
                .map(file -> save(file, uploadRequestPath, override))
                .collect(Collectors.groupingBy(o -> o.getIsSaved() ? "saved" : "notSaved", mapping(SavingResult::getFileModel, Collectors.toList())));
    }


    private SavingResult save(MultipartFile file, Path uploadRequestPath, Boolean override) {

        if (file == null || file.isEmpty())
            throw new EmptyFileException();

        String fileName = StringUtils.cleanPath(requireNonNull(file.getOriginalFilename()));
        Path relativePath = uploadRequestPath.resolve(fileName);
        Path absolutePath = uploadDirectory.resolve(relativePath);

        File oldFile = new File(absolutePath.toUri());

        if (!oldFile.exists() || override) {

            try {
                Files.copy(file.getInputStream(), absolutePath, StandardCopyOption.REPLACE_EXISTING);
                File savedFile = new File(absolutePath.toUri());
                FileTime modifiedFileTime = Files.getLastModifiedTime(savedFile.toPath(), LinkOption.NOFOLLOW_LINKS);
                LocalDateTime modifiedDate = LocalDateTime.ofInstant(modifiedFileTime.toInstant(), ZoneId.systemDefault());

                FileModel fileModel = FileModel.builder()
                        .name(savedFile.getName())
                        .path(absolutePath.toString())
                        .modifiedDate(modifiedDate)
                        .build();

                return new SavingResult(fileModel, true);
            } catch (IOException e) {
                //TODO add some handling here
                e.printStackTrace();
            }

        }

        FileModel fileModel = new FileModel(fileName, relativePath.toString());
        return new SavingResult(fileModel);
    }

    @Cacheable(cacheNames = "files", key = "#path")
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

            FileTime fileTime = null;
            try {
                fileTime = Files.getLastModifiedTime(entry.toPath(), LinkOption.NOFOLLOW_LINKS);
                LocalDateTime lastModified = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
                files.add(new FileModel(entry.getName(), path, entry.isDirectory(), lastModified));
            } catch (IOException e) {
                //TODO add handling for this
                e.printStackTrace();
            }
        }

        return files;
    }

    @CacheEvict(cacheNames = "files", key = "#path")
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

    public File getFile(String path) {

        String normalized = StringUtils.cleanPath(path);
        Path absolutePath = uploadDirectory.resolve(normalized);


        return new File(absolutePath.toUri());
    }

    @CacheEvict(cacheNames = "files", key = "#path")
    public FileModel updateName(String path, String oldName, String newName) {

        String normalizedPath = StringUtils.cleanPath(path);
        Path absolutePath = uploadDirectory.resolve(Paths.get(path, oldName));
        Path newAbsolutePath = uploadDirectory.resolve(Paths.get(normalizedPath, newName));

        File file = getFile(absolutePath.toString());
        boolean isRenamed = file.renameTo(new File(newAbsolutePath.toUri()));

        if (!isRenamed)
            throw new RenamingException("File could not be renamed");

        File renamedFile = new File(newAbsolutePath.toUri());
        return new FileModel(renamedFile.getName(), path, renamedFile.isDirectory());

    }
}
