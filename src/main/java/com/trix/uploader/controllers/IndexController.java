package com.trix.uploader.controllers;

import com.trix.uploader.services.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class IndexController {

    private final FileService fileService;

    public IndexController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String getIndex(@RequestParam("path") Optional<String> optionalPath, Model model) {
        String path = optionalPath.orElse("");
        model.addAttribute("files", fileService.getFilesUnderPath(path));
        model.addAttribute("currentPath", path.split("/"));
        return "index";
    }
}
