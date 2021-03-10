package org.example.app.services;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class UploadedFilesService {
    public File[] getAllFiles() {
        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        return dir.listFiles();
    }

    public void fileDownload(String fileName, HttpServletResponse response) throws IOException {
        String rootPath = System.getProperty("catalina.home");
        File file = new File(rootPath + File.separator + "external_uploads" + File.separator + fileName);
        InputStream is = new FileInputStream(file);
        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }
}
