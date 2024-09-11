package net.atos.service.search;

import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.model.DocumentEntity;
import net.atos.model.enums.EnumDataType;
import net.atos.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserSearchService extends AbstractSearchService {

    @Autowired
    public UserSearchService(DocumentRepository documentRepository) {
        super(documentRepository);
    }

    @Override
    public Set<DocumentEntity> search(String query) {
        String authenticatedEmail = CustomJwtAuthenticationConverter.extractUserEmailFromContext();
        Set<DocumentEntity> allDocuments = searchHelper(query);

        return allDocuments.stream().filter(
                documentEntity -> documentEntity.getAccessibleByUsers()
                        .contains(authenticatedEmail)).collect(Collectors.toSet());
    }

}