package net.atos.mapper;

import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.model.DocumentEntity;

public class DocumentMapper {

    public static DocumentReadOnlyDto mapToReadDocument (DocumentEntity entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity cannot be null");

        return new DocumentReadOnlyDto(
                entity.getId(),
                entity.getFilePath(),
                entity.getType(),
                entity.getSizeInBytes(),
                entity.getAccessibleByUsers(),
                entity.getTags(),
                entity.isPublic(),
                entity.getThumbnailPath(),
                entity.getLanguages(),
                entity.getAttributes(),
                entity.getDateOfCreation(),
                entity.getLastAccessed(),
                entity.getLastModified(),
                entity.getCreatedByUserId(),
                entity.getLastModifiedByUserId(),
                entity.getLastAccessedByUserId()
        );
    }

}