package net.atos.service.document;

import net.atos.dto.document.DocumentCreateDto;
import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;

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

    FileDownloadInfo downloadDocument(UUID id);

    PreviewFileResponse previewDocument(UUID id);

}