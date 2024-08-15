package net.atos.service;

import lombok.RequiredArgsConstructor;
import net.atos.dto.DocumentDto;
import net.atos.repository.TextBasedDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DocumentServiceImpl {

    private final TextBasedDocumentRepository textBasedDocumentRepository;

    public DocumentDto createDocument(DocumentDto documentDto) {
        return new DocumentDto();
    }

    public DocumentDto getDocument(UUID id) {
        return new DocumentDto();
    }

    public List<DocumentDto> getAllDocuments() {
        return new ArrayList<>();
    }

    public DocumentDto updateDocument(UUID id, DocumentDto documentDto) {
        return new DocumentDto();
    }

    public void deleteDocument(UUID id) {
    }
}
