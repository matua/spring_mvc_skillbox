package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.UploadedFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping(value = "/uploadedFiles")
public class UploadedFilesController {

    private final Logger logger = Logger.getLogger(UploadedFilesController.class);

    @Autowired
    public UploadedFilesService uploadedFilesService;

    @GetMapping
    public String uploadedFiles(Model model) {
        logger.info("GET /uploadedFiles returns uploadedFiles.html");
        model.addAttribute("files", uploadedFilesService.getAllFiles());
        return "uploadedFiles";
    }

    @GetMapping("/download/{fileName:.+}")
    public void downloadFile(HttpServletResponse response,
                             @PathVariable String fileName) throws IOException {
        logger.info("Trying to download file: " + fileName);

        String rootPath = System.getProperty("catalina.home");
        File file = new File(rootPath + File.separator + "external_uploads" + File.separator + fileName);

        InputStream is = new FileInputStream(file);
        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }
}