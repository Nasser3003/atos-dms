package net.atos.service;

import lombok.Getter;
import net.atos.exception.FileStorageException;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import net.atos.util.LocalFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
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

    public void storeFile(UUID userId, MultipartFile file, String pathInsideUserFolder) throws FileStorageException {
        if (file.isEmpty())
            throw new FileStorageException("Failed to store empty file.");

        Path fullPath = validateAndNormalizePath(userId, pathInsideUserFolder);

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

    public void renameFile(UUID owner, String oldPath, String newPath) throws FileStorageException {
        Path fullOldPath = validateAndNormalizePath(owner, oldPath);
        Path fullNewPath = validateAndNormalizePath(owner, newPath);

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
            throw new FileStorageException("Failed to rename file from " + fullOldPath + " to " +  fullNewPath, e);
        }
    }

    public Path getFilePathById(UUID documentId) throws FileStorageException {

        DocumentEntity documentEntity = documentRepository.findById(documentId).orElseThrow(
                () -> new FileStorageException("Document not found."));

        Path validatedPath = validateAndNormalizePath(documentEntity.getCreatedByUserId(), documentEntity.getFilePath());

        try (Stream<Path> pathStream = Files.walk(validatedPath.getParent(), 1)) {
            return pathStream
                    .filter(file -> file.getFileName().toString().equals(validatedPath.getFileName().toString()))
                    .findFirst()
                    .orElseThrow(() -> new FileStorageException("File not found: " + validatedPath.getFileName()));
        } catch (IOException ex) {
            throw new FileStorageException("Error accessing file: " + validatedPath.getFileName(), ex);
        }
    }

    public void deleteFile(UUID userId, String userSpecifiedPath) throws FileStorageException {
        Path fullPath = validateAndNormalizePath(userId,userSpecifiedPath);

        try {
            if (!Files.deleteIfExists(fullPath))
                throw new FileStorageException("File not found or could not be deleted: " + fullPath.getFileName());
        } catch (IOException ex) {
            throw new FileStorageException("Failed to delete file: " + fullPath.getFileName(), ex);
        }
    }

    public Path validateAndNormalizePath(UUID userId, String pathInsideUserFolder) {
        if (pathInsideUserFolder == null || pathInsideUserFolder.trim().isEmpty())
            throw new IllegalArgumentException("file name cannot be null or empty");

        Path userBasePath = baseStorageLocation.resolve(userId.toString());
        Path normalizedPath = Path.of(pathInsideUserFolder).normalize();

        if (normalizedPath.startsWith(userBasePath))
            return normalizedPath;
        return concatPathToUserIdFolder(userBasePath.toString(), normalizedPath.toString());
    }

    public MultipartFile getMultipartFile(Path filePath) throws IOException {
        return new CustomMultipartFile(filePath);
    }

    @Getter
    public static class CustomMultipartFile implements MultipartFile {

        private final Path filePath;
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] bytes;

        public CustomMultipartFile(Path filePath) throws IOException {
            this.filePath = filePath;
            this.name = filePath.getFileName().toString();
            this.originalFilename = this.name;
            this.contentType = Files.probeContentType(filePath);
            this.bytes = Files.readAllBytes(filePath);
        }

        @Override
        public boolean isEmpty() {
            return bytes.length == 0;
        }

        @Override
        public long getSize() {
            return bytes.length;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public void transferTo(File dest) throws IOException, IllegalStateException {
            try (OutputStream os = new FileOutputStream(dest)) {
                os.write(bytes);
            }
        }
    }

}

