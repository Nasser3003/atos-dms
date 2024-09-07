package net.atos.service.search;

import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.model.DocumentEntity;
import net.atos.model.enums.EnumDataType;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserSearchService extends AbstractSearchService {

    @Autowired
    public UserSearchService(DocumentRepository documentRepository) {
        super(documentRepository);
    }

    @Override
    public Set<DocumentEntity> search(String query) {
        UUID authenticatedId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        Set<DocumentEntity> allDocuments = searchHelper(query);

        return allDocuments.stream().filter(
                documentEntity -> documentEntity.getAccessibleByUsers()
                        .contains(authenticatedId)).collect(Collectors.toSet());
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