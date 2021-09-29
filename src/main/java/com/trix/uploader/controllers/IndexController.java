package com.trix.uploader.controllers;

import com.trix.uploader.services.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final FileService fileService;

    public IndexController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("files", fileService.getAllFileNames());
        return "index";
    }
}
