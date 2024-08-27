package net.atos.service.document;

import net.atos.dto.document.DocumentCreateDto;
import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface IDocumentService {
    DocumentReadOnlyDto createDocument(DocumentCreateDto createDto);

    List<DocumentReadOnlyDto> getAllNoneDeletedDocuments();

    List<DocumentReadOnlyDto> getAllDocuments();

    List<DocumentReadOnlyDto> getAllDeletedDocuments();

    DocumentReadOnlyDto getDocument(UUID id);

    DocumentReadOnlyDto updateDocument(DocumentEditDto documentEditDto);

    void deleteDocument(UUID id);

    ResponseEntity<Resource> downloadDocument(UUID id);
}