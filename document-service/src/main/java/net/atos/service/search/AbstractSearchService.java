package net.atos.service.search;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.model.DocumentEntity;
import net.atos.model.enums.EnumDataType;
import net.atos.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public abstract class AbstractSearchService {

    private final DocumentRepository documentRepository;

    public List<DocumentEntity> getAccessibleDocuments(List<DocumentEntity> documents) {
        UUID authenticatedId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        return documents.stream()
                .filter(documentEntity -> documentEntity.getAccessibleByUsers().contains(authenticatedId))
                .collect(Collectors.toList());
    }

    abstract List<DocumentEntity> searchDocumentsByType(EnumDataType type);

    abstract List<DocumentEntity> searchDocumentsByName(String name);

    abstract List<DocumentEntity> searchDocumentsByTag(String tag);
    
    List<DocumentEntity> searchDocumentsByTypeHelper(EnumDataType type) {
        return documentRepository.findAllByType(type);
    }

    List<DocumentEntity> searchDocumentsByNameHelper(String name) {
        return documentRepository.findAllByFilePathContaining(name);
    }

    List<DocumentEntity> searchDocumentsByTagHelper(String tag) {
        return documentRepository.findAllByTagsContainingIgnoreCase(tag);
    }

}