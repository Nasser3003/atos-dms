package net.atos.service;

import lombok.RequiredArgsConstructor;
import net.atos.dto.DocumentDto;
import net.atos.repository.TextBasedDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DocumentService {

    private final TextBasedDocumentRepository textBasedDocumentRepository;

    public DocumentDto createDocument(DocumentDto documentDto) {
    }

    public DocumentDto getDocument(UUID id) {
    }

    public List<DocumentDto> getAllDocuments() {
    }

    public DocumentDto updateDocument(UUID id, DocumentDto documentDto) {
    }

    public void deleteDocument(UUID id) {
    }
}
