package com.trix.uploader.services;

import com.trix.uploader.exceptions.fileException.FileException;
import com.trix.uploader.exceptions.fileOperationException.FileOperationException;
import com.trix.uploader.exceptions.path.PathException;
import com.trix.uploader.model.FileModel;
import com.trix.uploader.pojos.SavingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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

@Service
public class FileService {

    private final Path uploadDirectory;

    public FileService(@Value("${upload.location}") String filesPath) {
        this.uploadDirectory = Paths.get(filesPath);
    }

    private Path generateAbsoluteUploadPath(Path relativeUploadPath, String fileName) {

        if (relativeUploadPath.isAbsolute())
            throw new PathException("Path should not be absolute");

        Path cleanedFileName = Paths.get(fileName).getFileName();
        return uploadDirectory.resolve(relativeUploadPath.resolve(cleanedFileName));
    }

    private Path generateAbsoluteUploadPath(Path relativeUploadPath) {

        if (relativeUploadPath.isAbsolute())
            throw new PathException("Path should not be absolute");

        return uploadDirectory.resolve(relativeUploadPath);
    }

    private String getRelativePath(Path other) {
        return uploadDirectory.relativize(other).toString();
    }

    @CacheEvict(cacheNames = "files", keyGenerator = "parentKeyGen")
    public Map<String, List<FileModel>> saveAll(Path uploadRequestPath, List<MultipartFile> files, Boolean override) {

        return files.stream()
                .map(file -> save(file, uploadRequestPath, override))
                .collect(Collectors.groupingBy(
                        entry -> entry.getIsSaved() ? "saved" : "notSaved",
                        mapping(SavingResult::getFileModel, Collectors.toList())));
    }


    private SavingResult save(MultipartFile file, Path uploadRequestPath, Boolean override) {

        if (file == null || file.isEmpty())
            throw new FileException("File should not be empty");

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
                        getRelativePath(savedFile.toPath()),
                        savedFile.isDirectory(),
                        modifiedDate);

                return new SavingResult(fileModel, true);
            } catch (IOException e) {
                //TODO add some handling here
                e.printStackTrace();
            }

        }
        File file1 = new File(absolutePathUri);
        FileModel fileModel = new FileModel(file1.getName(), getRelativePath(file1.toPath()));
        return new SavingResult(fileModel);
    }

    @Cacheable(cacheNames = "files", keyGenerator = "parentKeyGen")
    public List<FileModel> getFilesUnderPath(String path) throws FileNotFoundException {

        ArrayList<FileModel> files = new ArrayList<>();
        Path relativePath = Paths.get(path);

        if (relativePath.isAbsolute())
            throw new PathException("Path should not be absolute");

        Path absolutePath = uploadDirectory.resolve(path);
        File file = new File(absolutePath.toUri());

        if (!file.exists())
            throw new FileNotFoundException();

        if (!file.isDirectory())
            throw new PathException("Path does not lead to directory");

        for (File entry : file.listFiles()) {

            Instant fileTime;

            try {
                fileTime = Files.getLastModifiedTime(entry.toPath(), LinkOption.NOFOLLOW_LINKS).toInstant();
            } catch (IOException e) {
                fileTime = Instant.EPOCH;
            }

            LocalDateTime lastModified = LocalDateTime.ofInstant(fileTime, ZoneId.systemDefault());
            files.add(new FileModel(entry.getName(), getRelativePath(entry.toPath()), entry.isDirectory(), lastModified));
        }

        return files;
    }

    @CacheEvict(cacheNames = "files", keyGenerator = "fileKeyGen")
    public FileModel createDirectory(String path) {

        Path relativePath = Paths.get(path);

        if (relativePath.isAbsolute())
            throw new PathException("Path should not be absolute");

        Path absolutePath = generateAbsoluteUploadPath(relativePath);

        File file = new File(absolutePath.toUri());
        FileModel fileModel;

        if (file.exists() && file.isDirectory()) {

            fileModel = new FileModel(file.getName(), getRelativePath(file.toPath()), true);

        } else {

            Path pathToDirectory;

            try {
                pathToDirectory = Files.createDirectory(absolutePath);
            } catch (IOException e) {
                pathToDirectory = absolutePath;
            }
            fileModel = new FileModel(file.getName(), getRelativePath(pathToDirectory), true);
        }

        return fileModel;

    }

    public FileInputStream getFileContent(String path) throws FileNotFoundException {

        File file = getFile(path);

        if (!file.exists() || file.isDirectory())
            throw new FileException("Can only read content of file from text files");

        return new FileInputStream(file);

    }

    public File getFile(String path) {

        String normalized = StringUtils.cleanPath(path);
        Path absolutePath = uploadDirectory.resolve(normalized);

        return new File(absolutePath.toUri());
    }

    @CacheEvict(cacheNames = "files", keyGenerator = "fileKeyGen")
    public FileModel updateName(String path, String oldName, String newName) {

        Path absolutePath = generateAbsoluteUploadPath(Paths.get(path));
        Path newAbsolutePath = absolutePath.resolveSibling(newName);

        File file = getFile(absolutePath.toString());
        boolean isRenamed = file.renameTo(new File(newAbsolutePath.toUri()));

        if (!isRenamed)
            throw new FileOperationException("File could not be renamed");

        File renamedFile = new File(newAbsolutePath.toUri());
        return new FileModel(renamedFile.getName(), getRelativePath(file.toPath()), renamedFile.isDirectory());

    }

    @CacheEvict(cacheNames = "files", keyGenerator = "fileKeyGen")
    public boolean delete(String path) {
        Path absolutePath = generateAbsoluteUploadPath(Paths.get(path));
        File file = new File(absolutePath.toUri());

        if (file.isDirectory()) {
            Arrays.stream(file.listFiles())
                    .forEach(entry -> delete(Paths.get(getRelativePath(file.toPath()), entry.getName()).toString()));
        }

        return file.delete();

    }

    @CacheEvict(value = "files", keyGenerator = "fileKeyGen")
    public FileModel createFileFromNote(String path, String note, String fileName, Boolean editing) throws FileAlreadyExistsException {
        Path absolutePath = generateAbsoluteUploadPath(Paths.get(path));

        File newFile = new File(absolutePath.toUri());

        if (!editing)
            if (newFile.exists())
                throw new FileAlreadyExistsException("File " + Paths.get(path, fileName) + " already exists. Please select different path or change file name");

        try {
            FileWriter fileWriter = new FileWriter(newFile);
            fileWriter.write(note);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new FileModel(newFile.getName(), getRelativePath(newFile.toPath()), false);
    }
}
