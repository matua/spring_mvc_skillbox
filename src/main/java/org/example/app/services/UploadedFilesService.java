package org.example.app.services;

import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class UploadedFilesService {
    public File[] getAllFiles() {
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");

        return dir.listFiles();
    }
}
