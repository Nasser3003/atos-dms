package net.atos.service.document;

import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.exception.FileDownloadException;
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
public class DocumentAdminService extends AbstractDocumentService {

    @Autowired
    public DocumentAdminService(DocumentRepository repository, LocalFileStorageService fileStorageService) {
        super(repository, fileStorageService);
    }

    @Override
    public List<DocumentReadOnlyDto> getAllDocuments() {
        return repository.findAll().stream()
                .map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentReadOnlyDto> getAllNoneDeletedDocuments() {
        return repository.findAll().stream()
                .filter(f -> !f.isDeleted())
                .map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentReadOnlyDto> getAllDeletedDocuments() {
        return repository.findAll().stream()
            .filter(DocumentEntity::isDeleted)
                .map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toList());    }

    @Override
    public DocumentReadOnlyDto getDocument(UUID id) {
        DocumentEntity documentEntity = findNoneDeletedDocumentById(id);
        return DocumentMapper.mapToReadDocument(documentEntity);
    }

    @Override
    @Transactional
    public DocumentReadOnlyDto updateDocument(DocumentEditDto documentEditDto) {
        if (documentEditDto == null)
            throw new IllegalArgumentException("documentEditDto cannot be null");

        return updateDocumentHelper(documentEditDto);
    }

    @Override
    public void deleteDocument(UUID id) {
        DocumentEntity documentEntity = findNoneDeletedDocumentById(id);
        documentEntity.setDeleted(true);
        repository.save(documentEntity);
    }

    @Override
    public FileDownloadInfo downloadDocument(UUID id) {
        try {
            return downloadFileHelper(id);
        } catch (IOException e) {
            throw new FileDownloadException("Error downloading file: ", e);
        }
    }

}
