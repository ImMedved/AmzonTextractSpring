package com.kukharev;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/*
In this class you can register the creation of a new window or work with the console.
You can also edit the messages received by the user, data entry methods
(if you need to select the desired file through the UI) and cases of message output.
 */

@Controller
public class UIController {

    private final FileProcessingService fileProcessingService;

    @Autowired
    public UIController(FileProcessingService fileProcessingService) {
        this.fileProcessingService = fileProcessingService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            fileProcessingService.processFile(file);
            model.addAttribute("message", "File processed successfully");
        } catch (Exception e) {
            model.addAttribute("message", "File processing failed: " + e.getMessage());
        }
        return "index";
    }
}
