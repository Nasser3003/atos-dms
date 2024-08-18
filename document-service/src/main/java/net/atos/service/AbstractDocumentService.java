package net.atos.service;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.DocumentCreateDto;
import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import net.atos.exception.DocumentNotFoundException;
import net.atos.mapper.DocumentMapper;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractDocumentService {

    protected final DocumentRepository repository;

    public DocumentReadOnlyDto createDocument(DocumentCreateDto createDto) {
        DocumentEntity documentEntity = new DocumentEntity(
                createDto.getPath(),
                createDto.getName(),
                createDto.getType(),
                createDto.getExtension(),
                createDto.getSizeInBytes(),
                CustomJwtAuthenticationConverter.extractUserIdFromContext()
        );
        repository.save(documentEntity);
        return DocumentMapper.mapToReadDocument(documentEntity);
    }

    public abstract DocumentReadOnlyDto getDocument(UUID id);

    public abstract DocumentReadOnlyDto updateDocument(DocumentEditDto documentEditDto);

    public abstract void deleteDocument(UUID id);

    boolean NotFileOwner(DocumentEntity document) {
        UUID userId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        return !document.getCreatedByUserId().equals(userId);
    }

    DocumentReadOnlyDto updateTheDocument(DocumentEditDto documentEditDto) {
        if (documentEditDto == null)
            throw new IllegalArgumentException("Entity cannot be null");

        DocumentEntity entity = findDocumentById(documentEditDto.getId());

        entity.setLastAccessedByUserId(CustomJwtAuthenticationConverter.extractUserIdFromContext());
        entity.setLastAccessed(LocalDateTime.now());

        return new DocumentReadOnlyDto(
                entity.getId(),
                entity.getPath(),
                entity.getName(),
                entity.getType(),
                entity.getSizeInBytes(),
                entity.getExtension(),
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

}