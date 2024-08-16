package net.atos.service;

import net.atos.dto.DocumentDto;
import net.atos.mapper.DocumentMapper;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentAdminService extends AbstractDocumentService {

    @Autowired
    public DocumentAdminService(DocumentRepository repository) {
        super(repository);
    }

    @Override
    public DocumentDto getDocument(UUID id) {
        DocumentEntity documentEntity = findDocumentById(id);
        return DocumentMapper.toDto(documentEntity);
    }

    public List<DocumentDto> getAllDocuments() {
        return repository.findAll().stream()
                .map(DocumentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentDto updateDocument(UUID id, DocumentDto documentDto) {
        // TODO
        return null;
    }

    @Override
    public void deleteDocument(UUID id) {
        findDocumentById(id);
        repository.deleteById(id);
    }
}
