package net.atos.service.document;

import net.atos.dto.document.DocumentCreateDto;
import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.dto.document.DocumentUserDto;

import java.util.List;
import java.util.UUID;

public interface IDocumentService {
    DocumentReadOnlyDto createDocument(DocumentCreateDto createDto);

    List<DocumentReadOnlyDto> getAllNoneDeletedDocuments();

    List<DocumentReadOnlyDto> getAllUserDocuments();

    List<DocumentReadOnlyDto> getAllNoneDeletedDocumentsForUser(String userEmail);

    List<DocumentReadOnlyDto> getAllDeletedDocuments();

    DocumentReadOnlyDto getDocument(UUID id);

    DocumentReadOnlyDto updateDocument(DocumentEditDto documentEditDto);

    void deleteDocument(UUID id);

    FileDownloadInfo downloadDocument(UUID id);

    PreviewFileResponse previewDocument(UUID id);

    DocumentReadOnlyDto addUser(DocumentUserDto documentUserDto);

    DocumentReadOnlyDto removeUser(DocumentUserDto documentUserDto);


}