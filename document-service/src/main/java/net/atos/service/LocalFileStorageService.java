package net.atos.service;

import net.atos.exception.FileStorageException;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import static net.atos.util.LocalFileUtil.concatPathToUserIdFolder;

@Service
public class LocalFileStorageService {

    private final Path baseStorageLocation;
    private final DocumentRepository documentRepository;

    @Autowired
    public LocalFileStorageService(@Value("${file.storage.location}") String baseStorageLocation, DocumentRepository documentRepository) {
        this.baseStorageLocation = Paths.get(baseStorageLocation).toAbsolutePath().normalize();
        this.documentRepository = documentRepository;
        try {
            Files.createDirectories(this.baseStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public void storeFile(MultipartFile file, String userSpecifiedPath, String sanitizedFileName) throws FileStorageException {
        if (file.isEmpty())
            throw new FileStorageException("Failed to store empty file.");

        Path fullPath = validateAndNormalizePath(userSpecifiedPath);

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            Files.createDirectories(fullPath);
            Path targetLocation = fullPath.resolve(sanitizedFileName);
            Files.copy(file.getInputStream(), targetLocation);
        } catch (IOException ex) {
            if (ex instanceof FileAlreadyExistsException)
                throw new FileStorageException("File already exists.");
            throw new FileStorageException("Failed to store file " + originalFilename, ex);
        }
    }

    public Path getFilePathById(UUID documentId) throws FileStorageException {

        DocumentEntity documentEntity = documentRepository.findById(documentId).orElseThrow(
                () -> new FileStorageException("Document not found."));

        Path fullPath = concatPathToUserIdFolder(documentEntity.getCreatedByUserId(), documentEntity.getFilePath());
        Path validatedPath = validateAndNormalizePath(fullPath.toString());


        try (Stream<Path> pathStream = Files.walk(validatedPath.getParent(), 1)) {
            return pathStream
                    .filter(file -> file.getFileName().toString().equals(validatedPath.getFileName().toString()))
                    .findFirst()
                    .orElseThrow(() -> new FileStorageException("File not found: " + validatedPath.getFileName()));
        } catch (IOException ex) {
            throw new FileStorageException("Error accessing file: " + validatedPath.getFileName(), ex);
        }
    }

    public void deleteFile(String userSpecifiedPath) throws FileStorageException {
        Path fullPath = validateAndNormalizePath(userSpecifiedPath);

        try {
            if (!Files.deleteIfExists(fullPath))
                throw new FileStorageException("File not found or could not be deleted: " + fullPath.getFileName());
        } catch (IOException ex) {
            throw new FileStorageException("Failed to delete file: " + fullPath.getFileName(), ex);
        }
    }

    private Path validateAndNormalizePath(String userSpecifiedPath) throws FileStorageException {
        if (userSpecifiedPath == null || userSpecifiedPath.trim().isEmpty())
            throw new FileStorageException("User specified path cannot be null or empty");

        try {
            Path fullPath = baseStorageLocation.resolve(userSpecifiedPath).normalize().toAbsolutePath();

            if (!fullPath.startsWith(baseStorageLocation))
                throw new FileStorageException("Access to file outside base storage location is not allowed: " + userSpecifiedPath);

            if (!Files.exists(fullPath.getParent()))
                throw new FileStorageException("Parent directory does not exist: " + fullPath.getParent());

            return fullPath;
        } catch (InvalidPathException ex) {
            throw new FileStorageException("Invalid path: " + userSpecifiedPath, ex);
        }
    }

}