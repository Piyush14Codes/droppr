package com.app.droppr.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.stream.Stream;

@Component
public class FileCleanupService {

    private final Path storageDirectory = Paths.get(System.getProperty("user.home"), "droppr-uploads");
    private static final Long EXPIRY_MINUTES = 5L;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void purgeExpiredFiles() {
        Instant cutoff = Instant.now().minusSeconds(EXPIRY_MINUTES * 60);

        try(Stream<Path> paths = Files.list(storageDirectory)) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> deleteIfExpired(path,cutoff));
        } catch (IOException e) {
            System.err.println("Cleanup task failed to scan directory: " + e.getMessage());
        }
    }

    private void deleteIfExpired(Path path, Instant cutoff) {
        try {
            FileTime lastModified = Files.getLastModifiedTime(path);
            if(lastModified.toInstant().isBefore(cutoff)) {
                Files.delete(path);
                System.out.println("Expired file deleted : " + path.getFileName());
            }
        } catch (IOException e) {
            System.err.println("Failed to delete " + path.getFileName() + " : " + e.getMessage());
        }
    }
}
