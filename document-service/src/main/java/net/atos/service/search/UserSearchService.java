package net.atos.service.search;

import net.atos.model.DocumentEntity;
import net.atos.model.enums.EnumDataType;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSearchService extends AbstractSearchService {

    @Autowired
    public UserSearchService(DocumentRepository documentRepository) {
        super(documentRepository);
    }

    @Override
    public List<DocumentEntity> searchDocumentsByType(EnumDataType type) {
        List<DocumentEntity> documents = searchDocumentsByTypeHelper(type);
        return getAccessibleDocuments(documents);
    }

    @Override
    public List<DocumentEntity> searchDocumentsByName(String name) {
        List<DocumentEntity> documents =  searchDocumentsByNameHelper(name);
        return getAccessibleDocuments(documents);
    }

    @Override
    public List<DocumentEntity> searchDocumentsByTag(String tag) {
        List<DocumentEntity> documents =  searchDocumentsByTagHelper(tag);
        return getAccessibleDocuments(documents);
    }

}