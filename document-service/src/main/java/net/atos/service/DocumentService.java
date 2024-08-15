package net.atos.service;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.DocumentDto;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentDto createDocument(DocumentDto documentDto) {
        DocumentEntity documentEntity = new DocumentEntity(
                documentDto.getPath(), documentDto.getName(), documentDto.getType(), documentDto.getExtension(),
                documentDto.getSizeInBytes(), CustomJwtAuthenticationConverter.extractUserId()
        );

        repository.save(documentEntity);
        // TODO
        return documentDto;
    }

    public DocumentDto getDocument(UUID id) {
        // TODO
        return null;
    }

    public List<DocumentDto> getAllDocuments() {
        // TODO
        return null;
    }

    public DocumentDto updateDocument(UUID id, DocumentDto documentDto) {
        // TODO
        return null;
    }

    public void deleteDocument(UUID id) {
        // TODO
    }
}
