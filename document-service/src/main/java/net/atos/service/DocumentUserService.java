package net.atos.service;

import net.atos.dto.DocumentDto;
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
    public DocumentDto getDocument(UUID id) {
        DocumentEntity documentEntity = findDocumentById(id);
        if (isFileOwner(documentEntity))
            return DocumentMapper.toDto(documentEntity);

        throw new YouDoNotHaveThePermissions("don't have the privileges for Document with id " + id);
    }

    @Override
    public DocumentDto updateDocument(UUID id, DocumentDto documentDto) {
        DocumentEntity documentEntity = findDocumentById(id);
        if (isFileOwner(documentEntity))
            // TODO
            return null;
        throw new YouDoNotHaveThePermissions("don't have the privileges for Document with id " + id);
    }

    @Override
    public void deleteDocument(UUID id) {
        DocumentEntity documentEntity = findDocumentById(id);
        if (isFileOwner(documentEntity))
            repository.deleteById(id);
        else
            throw new YouDoNotHaveThePermissions("don't have the privileges for Document with id " + id);
    }
}