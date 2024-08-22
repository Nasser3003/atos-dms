package net.atos.service;

import net.atos.exception.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class LocalFileStorageService {

    private final Path baseStorageLocation;

    public LocalFileStorageService(@Value("${file.storage.location}") String baseStorageLocation) {
        this.baseStorageLocation = Paths.get(baseStorageLocation).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.baseStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public void storeFile(MultipartFile file, String userSpecifiedPath, String sanitizedFileName) throws FileStorageException {
        if (file.isEmpty())
            throw new FileStorageException("Failed to store empty file.");

        Path fullPath = getValidatedPath(userSpecifiedPath);

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String fileNameOnDisk = sanitizedFileName + (fileExtension != null ? "." + fileExtension : "");

        try {
            Files.createDirectories(fullPath);
            Path targetLocation = fullPath.resolve(fileNameOnDisk);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new FileStorageException("Failed to store file " + originalFilename, ex);
        }
    }

    public Path getFilePath(UUID documentId, String userSpecifiedPath) {
        Path fullPath = getValidatedPath(userSpecifiedPath);

        try (Stream<Path> paths = Files.find(fullPath, 1,
                (path, attr) -> path.getFileName().toString().startsWith(documentId.toString()))) {
            return paths.findFirst()
                    .orElseThrow(() -> new FileNotFoundException("File not found for id: " + documentId));
        } catch (IOException ex) {
            throw new FileStorageException("Error accessing file with id: " + documentId, ex);
        }
    }

    public void deleteFile(UUID documentId, String userSpecifiedPath) {
        Path fullPath = getValidatedPath(userSpecifiedPath);

        try (Stream<Path> paths = Files.find(fullPath, 1,
                (path, attr) -> path.getFileName().toString().startsWith(documentId.toString()))) {
            Path filePath = paths.findFirst()
                    .orElseThrow(() -> new FileNotFoundException("File not found for id: " + documentId));
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("Failed to delete file with id: " + documentId, ex);
        }
    }

    private Path getValidatedPath(String userSpecifiedPath) {
        if (!StringUtils.hasText(userSpecifiedPath))
            throw new FileStorageException("User specified path cannot be empty.");

        Path fullPath = baseStorageLocation.resolve(Paths.get(userSpecifiedPath)).normalize();
        if (!fullPath.startsWith(baseStorageLocation))
            throw new FileStorageException("Cannot access file outside of the base storage location.");

        return fullPath;
    }
}