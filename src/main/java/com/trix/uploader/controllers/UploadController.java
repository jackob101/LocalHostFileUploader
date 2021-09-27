package com.trix.uploader.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class UploadController {

    private final String FILES_PATH = "./uploadContent/";

    @PostMapping("/upload")
    public String uploadFile(@PathParam("file")MultipartFile file, RedirectAttributes redirectAttributes){

        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("message","please select a file to upload");
            return "redirect:/";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Path path = Paths.get(FILES_PATH + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/";
    }

}
