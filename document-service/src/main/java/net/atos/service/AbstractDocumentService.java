package net.atos.service;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.DocumentCreateDto;
import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import net.atos.exception.DocumentNotFoundException;
import net.atos.exception.FileStorageException;
import net.atos.mapper.DocumentMapper;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractDocumentService {

    protected final DocumentRepository repository;
    protected final LocalFileStorageService fileStorageService;

    public DocumentReadOnlyDto createDocument(DocumentCreateDto createDto) {
        DocumentEntity documentEntity = new DocumentEntity(
                createDto.getFilePath(),
                createDto.getType(),
                createDto.getSizeInBytes(),
                CustomJwtAuthenticationConverter.extractUserIdFromContext()
        );
        repository.save(documentEntity);

        try {
            fileStorageService.storeFile(createDto.getFile(),
                    createDto.getFilePath(),
                    documentEntity.getId());
        } catch (FileStorageException e) {
            // If file storage fails, delete the database entry
            repository.delete(documentEntity);
            throw e;
        }

        return DocumentMapper.mapToReadDocument(documentEntity);
    }

    public abstract List<DocumentReadOnlyDto> getAllDocuments();

    public abstract DocumentReadOnlyDto getDocument(UUID id);

    public abstract DocumentReadOnlyDto updateDocument(DocumentEditDto documentEditDto);

    public abstract void deleteDocument(UUID id);

    boolean NotFileOwner(DocumentEntity document) {
        UUID userId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        return !document.getCreatedByUserId().equals(userId);
    }

    public abstract ResponseEntity<Resource> downloadDocument(UUID id);

    DocumentReadOnlyDto updateDocumentHelper(DocumentEditDto documentEditDto) {
        if (documentEditDto == null)
            throw new IllegalArgumentException("Entity cannot be null");

        DocumentEntity entity = findDocumentById(documentEditDto.getId());

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

    DocumentEntity findDocumentById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document with id " + id + " not found"));
    }

    ResponseEntity<Resource> downloadDocumentHelper(UUID id, DocumentEntity document) throws IOException {

        Path filePath = fileStorageService.getFilePath(document.getId(),
                document.getFilePath());

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


}