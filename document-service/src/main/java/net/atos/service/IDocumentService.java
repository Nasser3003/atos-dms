package net.atos.service;

DocumentDto;

import java.util.List;
import java.util.UUID;

public interface IDocumentService {
    DocumentDto createDocument(DocumentDto documentDto);
    DocumentDto getDocument(UUID id);
    List<DocumentDto> getAllDocuments();
    DocumentDto updateDocument(UUID id, DocumentDto documentDto);
    void deleteDocument(UUID id);
}