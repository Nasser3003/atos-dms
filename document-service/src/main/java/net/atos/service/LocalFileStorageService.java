package net.atos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService {

    private final Path fileStorageLocation;

    public LocalFileStorageService(@Value("${file.storage.location}") String fileStorageLocation) throws IOException {
        this.fileStorageLocation = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation);
        return fileName;
    }

    public Path getFilePath(String fileName) {
        return this.fileStorageLocation.resolve(fileName);
    }

    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = StringUtils.getFilenameExtension(originalFileName);
        return UUID.randomUUID() + (fileExtension != null ? "." + fileExtension : "");
    }
}