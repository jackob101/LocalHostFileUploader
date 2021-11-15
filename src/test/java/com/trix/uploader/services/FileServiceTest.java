package com.trix.uploader.services;

import com.trix.uploader.exceptions.EmptyFileException;
import com.trix.uploader.exceptions.NotATextFileException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    FileService fileService;

    String fileName = "textName.txt";
    String filesPath = "test";
    String fileContent = "This is example content of simple text file that is used for testing";

    @BeforeEach
    void setUp() throws IOException {
        fileService = new FileService(filesPath);
        File dummyFile = new File(Paths.get(filesPath, fileName).toUri());
        Files.createDirectories(dummyFile.toPath().getParent());
        Files.copy(new ByteArrayInputStream(fileContent.getBytes()), dummyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

    }

    @AfterEach
    void tearDown() {
//        File file = new File("./uploadContent/" + fileName);
//        file.delete();
    }

    @Test
    public void save_exception() {
        MultipartFile multipartFile = new MockMultipartFile(fileName, new byte[]{});

        assertThrows(EmptyFileException.class, () -> fileService.saveAll(List.of(multipartFile), Paths.get(""), false));
    }

    @Test
    public void save_success() throws IOException {
        //given
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        InputStream inputStream = new ByteArrayInputStream(data);
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, null, inputStream);

        //when
        fileService.saveAll(List.of(multipartFile), Paths.get(""), false);

    }

    @Test
    public void getFileContent_success() throws FileNotFoundException {

        FileInputStream result = fileService.getFileContent(fileName);

        String resultText = new BufferedReader(new InputStreamReader(result)).lines().collect(Collectors.joining());

        assertEquals(fileContent, resultText);

    }

    @Test
    public void getFileContent_throw() {

        assertThrows(NotATextFileException.class, () -> fileService.getFileContent("notExisting.png"));
    }

    @Test
    public void getFileContent_throwDirectory() throws IOException {
        String tempDir = "thisIsDirectory";
        Path dirPath = Paths.get(filesPath, tempDir);
        Files.createDirectory(dirPath);

        assertThrows(NotATextFileException.class,
                () -> fileService.getFileContent(dirPath.toString()));

        Files.delete(dirPath);

    }


}