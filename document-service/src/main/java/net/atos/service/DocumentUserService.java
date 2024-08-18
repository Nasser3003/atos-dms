package net.atos.service;

import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import net.atos.exception.YouDoNotHaveThePermissions;
import net.atos.mapper.DocumentMapper;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DocumentUserService extends AbstractDocumentService {

    @Autowired
    public DocumentUserService(DocumentRepository repository) {
        super(repository);
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

         return updateTheDocument(documentEditDto);
    }

    @Override
    public void deleteDocument(UUID id) {
        DocumentEntity documentEntity = findDocumentById(id);
        if (NotFileOwner(documentEntity))
            throw new YouDoNotHaveThePermissions("don't have the privileges for Document with id " + id);

        repository.deleteById(id);
    }
}