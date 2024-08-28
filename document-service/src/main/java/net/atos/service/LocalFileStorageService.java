package net.atos.service;

import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.exception.FileStorageException;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import net.atos.util.LocalFileUtil;
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

    public final Path baseStorageLocation;
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

    public void storeFile(MultipartFile file, String pathInsideUserFolder) throws FileStorageException {
        if (file.isEmpty())
            throw new FileStorageException("Failed to store empty file.");

        Path fullPath = validateAndNormalizePath(pathInsideUserFolder);

        String originalFilename = LocalFileUtil.extractFileName(pathInsideUserFolder);
        try {
            Files.createDirectories(Path.of(LocalFileUtil.extractDirectory(fullPath.toString())));
            Files.copy(file.getInputStream(), fullPath);
        } catch (IOException ex) {
            if (ex instanceof FileAlreadyExistsException)
                throw new FileStorageException("File already exists.");
            throw new FileStorageException("Failed to store file " + originalFilename, ex);
        }
    }

    public void renameFile(String oldPath, String newPath) throws FileStorageException {
        Path fullOldPath = validateAndNormalizePath(oldPath);
        Path fullNewPath = validateAndNormalizePath(newPath).resolve(LocalFileUtil.extractFileName(newPath));

        if (Files.exists(fullNewPath))
            throw new FileStorageException("there is a file with the same name at destination : " + newPath);

        if (!Files.exists(fullOldPath))
            throw new FileStorageException("File not found: " + oldPath);

        if (!Files.isRegularFile(fullOldPath))
            throw new FileStorageException("Not a file: " + oldPath);

        try {
            Path parent = fullNewPath.getParent();
            if (parent != null && !Files.exists(parent))
                Files.createDirectories(parent);
            Files.move(fullOldPath, fullNewPath, StandardCopyOption.ATOMIC_MOVE);

        } catch (FileAlreadyExistsException e) {
            throw new FileStorageException("A file with the new name already exists: " + LocalFileUtil.extractFileName(newPath));
        } catch (IOException e) {
            throw new FileStorageException("Failed to rename file from " + oldPath + " to " +  LocalFileUtil.extractFileName(newPath), e);
        }
    }

    public Path getFilePathById(UUID documentId) throws FileStorageException {

        DocumentEntity documentEntity = documentRepository.findById(documentId).orElseThrow(
                () -> new FileStorageException("Document not found."));

        Path fullPath = concatPathToUserIdFolder(documentEntity.getCreatedByUserId(), documentEntity.getFilePath());
        Path validatedPath = validateAndNormalizePath(CustomJwtAuthenticationConverter.extractUserIdFromContext(),fullPath.toString());


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

    private Path validateAndNormalizePath(UUID userId, String relativePath) {
        if (relativePath == null || relativePath.trim().isEmpty())
            throw new IllegalArgumentException("Relative path cannot be null or empty");

        Path userBasePath = baseStorageLocation.resolve(userId.toString());
        Path inputPath = Path.of(relativePath).normalize();

        if (inputPath.startsWith(userBasePath))
            return inputPath;
        return userBasePath.resolve(inputPath);
    }
    private Path validateAndNormalizePath(String relativePath){
        return validateAndNormalizePath(CustomJwtAuthenticationConverter.extractUserIdFromContext(), relativePath);
    }

}