package com.trix.uploader.services;

import com.trix.uploader.exceptions.EmptyFileException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    FileService fileService;

    String fileName = "textName.txt";

    @BeforeEach
    void setUp() {
        fileService = new FileService();
    }

    @AfterEach
    void tearDown() {
        File file = new File("./uploadContent/" + fileName);
        file.delete();
    }

    @Test
    public void save_exception() {
        MultipartFile multipartFile = new MockMultipartFile(fileName, new byte[]{});

        assertThrows(EmptyFileException.class, () -> fileService.save(multipartFile));
    }

    @Test
    public void save_success() throws IOException {
        //given
        byte[] data = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        InputStream inputStream = new ByteArrayInputStream(data);
        MultipartFile multipartFile = new MockMultipartFile(fileName, fileName, null, inputStream);

        //when
        fileService.save(multipartFile);
    }
}