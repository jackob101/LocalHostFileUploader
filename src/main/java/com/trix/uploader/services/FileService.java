package com.trix.uploader.services;

import com.trix.uploader.exceptions.EmptyFileException;
import com.trix.uploader.exceptions.directory.NotADirectoryException;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;

@Cacheable("files")
@Service
public class FileService {

    private final Path uploadDirectory;

    public FileService(@Value("${upload.location}") String filesPath) {
        this.uploadDirectory = Paths.get(filesPath);
    }

    private Path generateAbsoluteUploadPath(Path relativeUploadPath, String fileName) {

        if (relativeUploadPath.isAbsolute())
            throw new AbsolutePathException();

        Path cleanedFileName = Paths.get(fileName).getFileName();
        return uploadDirectory.resolve(relativeUploadPath.resolve(cleanedFileName));
    }

    @CacheEvict(cacheNames = "files", key = "#uploadRequestPath.toString()")
    public Map<String, List<FileModel>> saveAll(List<MultipartFile> files, Path uploadRequestPath, Boolean override) {

        return files.stream()
                .map(file -> save(file, uploadRequestPath, override))
                .collect(Collectors.groupingBy(
                        entry -> entry.getIsSaved() ? "saved" : "notSaved",
                        mapping(SavingResult::getFileModel, Collectors.toList())));
    }


    private SavingResult save(MultipartFile file, Path uploadRequestPath, Boolean override) {

        if (file == null || file.isEmpty())
            throw new EmptyFileException();

        Path absolutePath = generateAbsoluteUploadPath(uploadRequestPath, file.getOriginalFilename());
        URI absolutePathUri = absolutePath.toUri();

        boolean exists = Files.exists(absolutePath, LinkOption.NOFOLLOW_LINKS);

        if (!exists || override) {

            try {

                Files.copy(file.getInputStream(), absolutePath, StandardCopyOption.REPLACE_EXISTING);
                File savedFile = new File(absolutePathUri);

                Instant modifiedInstant = Files.getLastModifiedTime(savedFile.toPath(), LinkOption.NOFOLLOW_LINKS).toInstant();
                LocalDateTime modifiedDate = LocalDateTime.ofInstant(modifiedInstant, ZoneId.systemDefault());

                FileModel fileModel = new FileModel(
                        savedFile.getName(),
                        uploadDirectory.resolve(absolutePath).toString(),
                        savedFile.isDirectory(),
                        modifiedDate);

                return new SavingResult(fileModel, true);
            } catch (IOException e) {
                //TODO add some handling here
                e.printStackTrace();
            }

        }

        FileModel fileModel = new FileModel(file.getName(), uploadRequestPath.toString());
        return new SavingResult(fileModel);
    }

    @Cacheable(cacheNames = "files", key = "#path")
    public List<FileModel> getFilesUnderPath(String path) throws FileNotFoundException {

        ArrayList<FileModel> files = new ArrayList<>();
        Path relativePath = Paths.get(path);

        if (relativePath.isAbsolute())
            throw new AbsolutePathException();

        Path absolutePath = uploadDirectory.resolve(path);
        File file = new File(absolutePath.toUri());

        if (!file.exists())
            throw new FileNotFoundException();

        if (!file.isDirectory())
            throw new NotADirectoryException();

        for (File entry : file.listFiles()) {

            Instant fileTime = null;

            try {
                fileTime = Files.getLastModifiedTime(entry.toPath(), LinkOption.NOFOLLOW_LINKS).toInstant();
            } catch (IOException e) {
                fileTime = Instant.EPOCH;
            }

            LocalDateTime lastModified = LocalDateTime.ofInstant(fileTime, ZoneId.systemDefault());
            files.add(new FileModel(entry.getName(), path, entry.isDirectory(), lastModified));
        }

        return files;
    }

    @CacheEvict(cacheNames = "files", key = "#path")
    public FileModel createDirectory(String path, String directoryName) {

        Path relativePath = Paths.get(path);

        if (relativePath.isAbsolute())
            throw new AbsolutePathException();

        Path absolutePath = generateAbsoluteUploadPath(relativePath, directoryName);

        File file = new File(absolutePath.toUri());
        FileModel fileModel = null;

        if (file.exists() && file.isDirectory()) {

            fileModel = new FileModel(file.getName(), path, true);

        } else {

            Path pathToDirectory;

            try {
                pathToDirectory = Files.createDirectory(absolutePath);
            } catch (IOException e) {
                pathToDirectory = absolutePath;
            }
            fileModel = new FileModel(directoryName, relativePath.toString(), true);
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

    @CacheEvict(cacheNames = "files", key = "#path")
    public boolean delete(String path, String fileName) {

        Path absolutePath = generateAbsoluteUploadPath(Paths.get(path), fileName);

        File file = new File(absolutePath.toUri());

        if (file.isDirectory()) {
            Arrays.stream(file.listFiles())
                    .forEach(entry -> delete(uploadDirectory.relativize(file.toPath()).toString(), entry.getName()));
        }

        return file.delete();

    }
}
