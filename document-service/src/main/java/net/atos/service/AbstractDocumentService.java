package net.atos.service;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.DocumentCreateDto;
import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import net.atos.exception.DocumentNotFoundException;
import net.atos.exception.FileNotFoundException;
import net.atos.mapper.DocumentMapper;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import net.atos.util.LocalFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.atos.util.LocalFileUtil.extractDirectory;
import static net.atos.util.LocalFileUtil.extractFileName;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractDocumentService implements IDocumentService {

    protected final DocumentRepository repository;
    protected final LocalFileStorageService fileStorageService;

    @Override
    @Transactional
    public DocumentReadOnlyDto createDocument(DocumentCreateDto createDto) {
        UUID userId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        String sanitizedFileName = sanitizeFileName(Objects.requireNonNull(extractFileName(createDto.getFilePath())));
        String relativePath = LocalFileUtil.concatPathToUserIdFolder(userId, extractDirectory(createDto.getFilePath())).toString();

        DocumentEntity documentEntity = new DocumentEntity(
                sanitizedFileName,
                createDto.getType(),
                createDto.getSizeInBytes(),
                userId
        );

        fileStorageService.storeFile(createDto.getFile(), relativePath, sanitizedFileName);
        repository.save(documentEntity);

        return DocumentMapper.mapToReadDocument(documentEntity);
    }

    public abstract List<DocumentReadOnlyDto> getAllDocuments();

    public abstract List<DocumentReadOnlyDto> getAllNoneDeletedDocuments();

    public abstract List<DocumentReadOnlyDto> getAllDeletedDocuments();

    public abstract DocumentReadOnlyDto getDocument(UUID id);

    public abstract DocumentReadOnlyDto updateDocument(DocumentEditDto documentEditDto);

    public abstract void deleteDocument(UUID id);

    public abstract ResponseEntity<Resource> downloadDocument(UUID id);

    boolean NotFileOwner(DocumentEntity document) {
        UUID userId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        return !document.getCreatedByUserId().equals(userId);
    }

    DocumentReadOnlyDto updateDocumentHelper(DocumentEditDto documentEditDto) {
        if (documentEditDto == null)
            throw new IllegalArgumentException("Entity cannot be null");

        DocumentEntity entity = findNoneDeletedDocumentById(documentEditDto.getId());

        entity.setFilePath(documentEditDto.getFilePath());
        entity.setType(documentEditDto.getType());
        entity.setTags(documentEditDto.getTags());
        entity.setPublic(documentEditDto.getIsPublic());

        entity.setLastAccessedByUserId(CustomJwtAuthenticationConverter.extractUserIdFromContext());
        entity.setLastAccessed(LocalDateTime.now());

        repository.save(entity);

        return new DocumentReadOnlyDto(
                entity.getId(),
                entity.getFilePath(),
                entity.getType(),
                entity.getSizeInBytes(),
                entity.getAccessibleByUsers(),
                entity.getTags(),
                entity.isPublic(),
                entity.getThumbnailPath(),
                entity.getLanguages(),
                entity.getAttributes(),
                entity.getDateOfCreation(),
                entity.getLastAccessed(),
                entity.getLastModified(),
                entity.getCreatedByUserId(),
                entity.getLastModifiedByUserId(),
                entity.getLastAccessedByUserId()
        );
    }

    DocumentEntity findNoneDeletedDocumentById(UUID id) {
        DocumentEntity documentEntity = findDocumentById(id);
        if (documentEntity.isDeleted())
            throw new FileNotFoundException("file doesnt exist or already deleted");
        return documentEntity;
    }

    private DocumentEntity findDocumentById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document with id " + id + " not found"));
    }

    ResponseEntity<Resource> downloadDocumentHelper(DocumentEntity document) throws IOException {

        Path filePath = fileStorageService.getFilePathById(document.getId());

        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists() || resource.isReadable()) {
            String contentType = Files.probeContentType(filePath);
            if (contentType == null)
                contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }
        else
            throw new FileNotFoundException("Could not read file: " + document.getFilePath());
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }


}