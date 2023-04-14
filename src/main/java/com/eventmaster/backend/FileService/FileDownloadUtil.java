package com.eventmaster.backend.FileService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//TODO: Das würde ich zu dem DocumentService dazunehmen, weils auch keine extra FileDownload Klasse im Diagramm gibt
public class FileDownloadUtil {
    private Path foundFile;

    public Resource getFileAsResource(long eventId, String fileCode) throws IOException {
        Path dirPath = Paths.get("/media/eventfiles/event/" + eventId);

        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                foundFile = file;
                return;
            }
        });

        if (foundFile != null) {
            return new UrlResource(foundFile.toUri());
        }

        return null;
    }
}
