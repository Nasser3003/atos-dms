package net.atos.mapper;

import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.DocumentDto;
import net.atos.model.DocumentEntity;

public class DocumentMapper {

    public static DocumentDto toDto(DocumentEntity documentEntity) {
        if (documentEntity == null) {
            return null;
        }
        return DocumentDto.builder()
                .id(documentEntity.getId())
                .path(documentEntity.getPath())
                .name(documentEntity.getName())
                .type(documentEntity.getType())
                .extension(documentEntity.getExtension())
                .dateOfCreation(documentEntity.getDateOfCreation())
                .lastAccessed(documentEntity.getLastAccessed())
                .lastModified(documentEntity.getLastModified())
                .sizeInBytes(documentEntity.getSizeInBytes())
                .createdByUserId(documentEntity.getCreatedByUserId())
                .accessibleByUsers(documentEntity.getAccessibleByUsers())
                .lastModifiedByUserId(documentEntity.getLastModifiedByUserId())
                .tags(documentEntity.getTags())
                .isPublic(documentEntity.isPublic())
                .thumbnailPath(documentEntity.getThumbnailPath())
                .languages(documentEntity.getLanguages())
                .attributes(documentEntity.getAttributes())
                .build();
    }

    public static DocumentEntity toEntity(DocumentDto documentDto) {
        if (documentDto == null) {
            return null;
        }
        DocumentEntity entity = new DocumentEntity(
                documentDto.getPath(),
                documentDto.getName(),
                documentDto.getType(),
                documentDto.getExtension(),
                documentDto.getSizeInBytes(),
                CustomJwtAuthenticationConverter.extractUserId()
        );

        entity.setTags(documentDto.getTags());
        entity.setPublic(documentDto.isPublic());
        entity.setThumbnailPath(documentDto.getThumbnailPath());
        entity.setLanguages(documentDto.getLanguages());
        entity.setAttributes(documentDto.getAttributes());

        return entity;
    }
}