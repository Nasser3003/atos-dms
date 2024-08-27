package net.atos.service.document;

import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.exception.YouDoNotHaveThePermissions;
import net.atos.mapper.DocumentMapper;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import net.atos.service.LocalFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentUserService extends AbstractDocumentService {

    @Autowired
    public DocumentUserService(DocumentRepository repository, LocalFileStorageService localFileStorageService) {
        super(repository, localFileStorageService);
    }

    @Override
    public List<DocumentReadOnlyDto> getAllDocuments() {
        UUID currentUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        return repository.findAll().stream()
                .filter(f -> !f.isDeleted())
                .filter(f -> f.getCreatedByUserId().equals(currentUserId))
                .map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentReadOnlyDto> getAllNoneDeletedDocuments() {
        UUID currentUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        return repository.findAll().stream()
                .filter(f -> !f.isDeleted())
                .filter(f -> f.getCreatedByUserId().equals(currentUserId))
                .map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentReadOnlyDto> getAllDeletedDocuments() {
        UUID currentUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        return repository.findAll().stream()
                .filter(DocumentEntity::isDeleted)
                .filter(f -> f.getCreatedByUserId().equals(currentUserId))
                .map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toList());    }

    @Override
    public DocumentReadOnlyDto getDocument(UUID id) {
        DocumentEntity documentEntity = findNoneDeletedDocumentById(id);
        if (NotFileOwner(documentEntity))
            throw new YouDoNotHaveThePermissions("don't have the privileges for Document with id " + id);
        return DocumentMapper.mapToReadDocument(documentEntity);
    }

    @Override
    public DocumentReadOnlyDto updateDocument(DocumentEditDto documentEditDto) {
        UUID id = documentEditDto.getId();
        DocumentEntity entity = findNoneDeletedDocumentById(id);
        if (NotFileOwner(entity))
            throw new YouDoNotHaveThePermissions("don't have the privileges for Document with id " + id);

         return updateDocumentHelper(documentEditDto);
    }

    @Override
    public void deleteDocument(UUID id) {
        DocumentEntity documentEntity = findNoneDeletedDocumentById(id);
        if (NotFileOwner(documentEntity))
            throw new YouDoNotHaveThePermissions("don't have the privileges for Document with id " + id);
        documentEntity.setDeleted(true);
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {
        DocumentEntity document = findNoneDeletedDocumentById(id);
        if (NotFileOwner(document))
            throw new YouDoNotHaveThePermissions("You don't have the privileges to download Document with id " + id);

        try {
            return downloadDocumentHelper(document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}