package com.trix.uploader.controllers;

import com.trix.uploader.services.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.websocket.server.PathParam;

@Controller
public class UploadController {

    private final String FILES_PATH = "./uploadContent/";
    private final FileService fileService;

    public UploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public String uploadFile(@PathParam("file")MultipartFile file, RedirectAttributes redirectAttributes){

        fileService.save(file);

        return "redirect:/";
    }

}
