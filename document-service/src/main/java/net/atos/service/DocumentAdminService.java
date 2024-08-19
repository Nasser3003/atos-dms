package net.atos.service;

import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
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
    public DocumentAdminService(DocumentRepository repository, LocalFileStorageService fileStorageService) {
        super(repository, fileStorageService);
    }

    @Override
    public DocumentReadOnlyDto getDocument(UUID id) {
        DocumentEntity documentEntity = findDocumentById(id);
        return DocumentMapper.mapToReadDocument(documentEntity);
    }

    public List<DocumentReadOnlyDto> getAllDocuments() {
        return repository.findAll().stream()
                .map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentReadOnlyDto updateDocument(DocumentEditDto documentEditDto) {
        if (documentEditDto == null)
            throw new IllegalArgumentException("documentEditDto cannot be null");

        return updateTheDocument(documentEditDto);
    }

    @Override
    public void deleteDocument(UUID id) {
        findDocumentById(id);
        repository.deleteById(id);
    }
}
