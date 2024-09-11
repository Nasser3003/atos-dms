package net.atos.service.search;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.model.DocumentEntity;
import net.atos.model.enums.EnumDataType;
import net.atos.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public abstract class AbstractSearchService {

    private final DocumentRepository documentRepository;

    List<DocumentEntity> getAccessibleDocuments(List<DocumentEntity> documents) {
        String authenticatedEmail = CustomJwtAuthenticationConverter.extractUserEmailFromContext();

        return documents.stream()
                .filter(documentEntity -> !documentEntity.isDeleted())
                .filter(documentEntity -> documentEntity.getAccessibleByUsers().contains(authenticatedEmail))
                .collect(Collectors.toList());
    }

    abstract Set<DocumentEntity> search(String query);

    Set<DocumentEntity> searchHelper(String query) {
        List<DocumentEntity> allResults = new ArrayList<>();
        List<DocumentEntity> searchByName = searchDocumentsByNameHelper(query);
        List<DocumentEntity> searchByTag = searchDocumentsByTagHelper(query);
        List<DocumentEntity> searchByType = searchDocumentsByPartialType(query);

        allResults.addAll(searchByName);
        allResults.addAll(searchByTag);
        allResults.addAll(searchByType);

        return new HashSet<>(allResults);
    }

    List<DocumentEntity> searchDocumentsByTypeHelper(EnumDataType type) {
        return documentRepository.findAllByType(type).stream()
                .filter(documentEntity -> !documentEntity.isDeleted()).collect(Collectors.toList());
    }

    List<DocumentEntity> searchDocumentsByNameHelper(String name) {
        if (name == null || name.isEmpty())
            return new ArrayList<>();
        return documentRepository.findAllByFilePathContainingIgnoreCase(name).stream()
                .filter(documentEntity -> !documentEntity.isDeleted()).collect(Collectors.toList());
    }

    List<DocumentEntity> searchDocumentsByTagHelper(String tag) {
        if (tag == null || tag.isEmpty())
            return new ArrayList<>();
        return documentRepository.findAllByTagsContainingIgnoreCase(tag).stream()
                .filter(documentEntity -> !documentEntity.isDeleted()).collect(Collectors.toList());
    }

    List<DocumentEntity> searchDocumentsByPartialType(String partialType) {
        if (partialType == null || partialType.isEmpty())
            return new ArrayList<>();

        String lowerCaseSearchTerm = partialType.toLowerCase();

        List<EnumDataType> matchingTypes = Arrays.stream(EnumDataType.values())
                .filter(enumValue -> enumValue.name().toLowerCase().contains(lowerCaseSearchTerm))
                .collect(Collectors.toList());

        if (matchingTypes.isEmpty())
            return new ArrayList<>();

        return matchingTypes.stream()
                .flatMap(type -> searchDocumentsByTypeHelper(type).stream())
                .collect(Collectors.toList())
                .stream().filter(documentEntity -> !documentEntity.isDeleted()).collect(Collectors.toList());
    }

}