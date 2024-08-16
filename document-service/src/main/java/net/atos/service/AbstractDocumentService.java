package net.atos.service;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.DocumentDto;
import net.atos.exception.DocumentNotFoundException;
import net.atos.mapper.DocumentMapper;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import net.atos.util.DocumentValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractDocumentService {

    protected final DocumentRepository repository;

    public DocumentDto createDocument(DocumentDto documentDto) {
        DocumentValidator.validateDocument(documentDto);
        DocumentEntity documentEntity = DocumentMapper.toEntity(documentDto);
        repository.save(documentEntity);
        return DocumentMapper.toDto(documentEntity);
    }

    public abstract DocumentDto getDocument(UUID id);

    public abstract DocumentDto updateDocument(UUID id, DocumentDto documentDto);

    public abstract void deleteDocument(UUID id);

    protected boolean isFileOwner(DocumentEntity document) {
        UUID userId = CustomJwtAuthenticationConverter.extractUserId();
        return document.getCreatedByUserId().equals(userId);
    }

    protected DocumentEntity findDocumentById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException("Document with id " + id + " not found"));
    }
}