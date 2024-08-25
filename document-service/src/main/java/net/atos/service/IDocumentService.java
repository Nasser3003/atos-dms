package net.atos.service;

import net.atos.dto.DocumentCreateDto;
import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IDocumentService {
    DocumentReadOnlyDto createDocument(DocumentCreateDto createDto);
    List<DocumentReadOnlyDto> getAllDocuments();
    DocumentReadOnlyDto getDocument(UUID id);
    DocumentReadOnlyDto updateDocument(DocumentEditDto documentEditDto);
    void deleteDocument(UUID id);
    ResponseEntity<Resource> downloadDocument(UUID id);
}