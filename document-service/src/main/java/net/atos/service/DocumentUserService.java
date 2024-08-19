package net.atos.service;

import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import net.atos.exception.YouDoNotHaveThePermissions;
import net.atos.mapper.DocumentMapper;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentUserService extends AbstractDocumentService {

    @Autowired
    public DocumentUserService(DocumentRepository repository, LocalFileStorageService localFileStorageService) {
        super(repository, localFileStorageService);
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {
        DocumentEntity document = findDocumentById(id);
        if (NotFileOwner(document))
            throw new YouDoNotHaveThePermissions("You don't have the privileges to download Document with id " + id);

        try {
            return downloadDocumentHelper(id, document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DocumentReadOnlyDto> getAllDocuments() {
        List<DocumentEntity> allFiles = repository.findAll();
        List<DocumentReadOnlyDto> documentReadOnlyDtos = new ArrayList<>();
        for (DocumentEntity entity : allFiles) {
            if (entity.getCreatedByUserId() == CustomJwtAuthenticationConverter.extractUserIdFromContext())
                documentReadOnlyDtos.add(DocumentMapper.mapToReadDocument(entity));
        }
        return documentReadOnlyDtos;
    }

    @Override
    public DocumentReadOnlyDto getDocument(UUID id) {
        DocumentEntity documentEntity = findDocumentById(id);
        if (NotFileOwner(documentEntity))
            throw new YouDoNotHaveThePermissions("don't have the privileges for Document with id " + id);
        return DocumentMapper.mapToReadDocument(documentEntity);
    }

    @Override
    public DocumentReadOnlyDto updateDocument(DocumentEditDto documentEditDto) {
        UUID id = documentEditDto.getId();
        DocumentEntity entity = findDocumentById(id);
        if (NotFileOwner(entity))
            throw new YouDoNotHaveThePermissions("don't have the privileges for Document with id " + id);

         return updateDocumentHelper(documentEditDto);
    }

    @Override
    public void deleteDocument(UUID id) {
        DocumentEntity documentEntity = findDocumentById(id);
        if (NotFileOwner(documentEntity))
            throw new YouDoNotHaveThePermissions("don't have the privileges for Document with id " + id);

        repository.deleteById(id);
    }


}