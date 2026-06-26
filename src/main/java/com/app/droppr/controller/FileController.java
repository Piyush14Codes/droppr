package com.app.droppr.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final Path storageDirectory = Paths.get(System.getProperty("user.home"), "droppr-uploads");

    public FileController() throws IOException {
        if (!Files.exists(storageDirectory)) {
            Files.createDirectory(storageDirectory);
        }
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "Upload failed: File is empty";
        }

        Path destination = storageDirectory.resolve(file.getOriginalFilename());
        file.transferTo(destination);

        return "Uploaded: " + file.getOriginalFilename();
    }

    @GetMapping
    public List<String> listFiles() throws IOException {
        try (Stream<Path> paths = Files.list(storageDirectory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        Path filepath = storageDirectory.resolve(filename);

        if(!Files.exists(filepath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filepath.toUri());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

}
