package net.atos.service.search;

import net.atos.model.DocumentEntity;
import net.atos.model.enums.EnumDataType;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminSearchService extends AbstractSearchService {

    @Autowired
    public AdminSearchService(DocumentRepository documentRepository) {
        super(documentRepository);
    }


    @Override
    public List<DocumentEntity> searchDocumentsByType(EnumDataType type) {
        return searchDocumentsByTypeHelper(type);
    }

    @Override
    public List<DocumentEntity> searchDocumentsByName(String name) {
        return searchDocumentsByNameHelper(name);
    }

    @Override
    public List<DocumentEntity> searchDocumentsByTag(String tag) {
        return searchDocumentsByTagHelper(tag);
    }

    @Override
    public List<DocumentEntity> searchIsDeleted(boolean isDeleted) {
        return searchIsDeletedHelper(isDeleted);
    }
}