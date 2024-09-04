package net.atos.service.document;

import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.exception.FileDownloadException;
import net.atos.exception.UnauthorizedException;
import net.atos.mapper.DocumentMapper;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import net.atos.service.LocalFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<DocumentReadOnlyDto> getAllNoneDeletedDocuments() {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        return repository.findAll().stream()
                .filter(f -> !f.isDeleted())
                .filter(f -> f.getAccessibleByUsers().contains(authenticatedUserId))
                .map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentReadOnlyDto> getAllUserDocuments() {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        return repository.findAll().stream()
                .filter(documentEntity -> documentEntity.getAccessibleByUsers().contains(authenticatedUserId))
                .map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toList());    }

    @Override
    public List<DocumentReadOnlyDto> getAllNoneDeletedDocumentsForUser(UUID userId) {
        throw new UnsupportedOperationException("This operation is not supported for this user type");
    }

    @Override
    public List<DocumentReadOnlyDto> getAllDeletedDocuments() {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        return repository.findAll().stream()
                .filter(DocumentEntity::isDeleted)
                .filter(f -> f.getAccessibleByUsers().contains(authenticatedUserId))
                .map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toList());    }

    @Override
    public DocumentReadOnlyDto getDocument(UUID id) {
        DocumentEntity documentEntity = findNoneDeletedDocumentById(id);
        if (!documentEntity.isUserAuthorized(id))
            throw new UnauthorizedException("don't have the privileges for Document with id " + id);
        return DocumentMapper.mapToReadDocument(documentEntity);
    }

    @Override
    @Transactional
    public DocumentReadOnlyDto updateDocument(DocumentEditDto documentEditDto) {
        UUID id = documentEditDto.getId();
        DocumentEntity entity = findNoneDeletedDocumentById(id);
        if (NotFileOwner(entity))
            throw new UnauthorizedException("don't have the privileges for Document with id " + id);

         return updateDocumentHelper(documentEditDto);
    }

    @Override
    public void deleteDocument(UUID id) {
        DocumentEntity documentEntity = findNoneDeletedDocumentById(id);
        if (NotFileOwner(documentEntity))
            throw new UnauthorizedException("don't have the privileges for Document with id " + id);
        documentEntity.setDeleted(true);
        repository.save(documentEntity);
    }

    @Override
    public FileDownloadInfo downloadDocument(UUID id) {
        DocumentEntity documentEntity = findNoneDeletedDocumentById(id);
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        if (!documentEntity.isUserAuthorized(authenticatedUserId))
            throw new UnauthorizedException("don't have the privileges for Document with id " + id);
        try {
            return downloadFileHelper(id);
        } catch (IOException e) {
            throw new FileDownloadException("Error downloading file: ", e);
        }
    }

    @Override
    public PreviewFileResponse previewDocument(UUID id) {
        DocumentEntity documentEntity = findNoneDeletedDocumentById(id);
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        if (!documentEntity.isUserAuthorized(authenticatedUserId))
            throw new UnauthorizedException("don't have the privileges for Document with id " + id);
        return previewFileHelper(id);
    }
}