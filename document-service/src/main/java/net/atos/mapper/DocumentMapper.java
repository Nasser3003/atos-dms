package net.atos.mapper;

import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.model.DocumentEntity;

import java.util.stream.Collectors;

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
                entity.getCreatedByUser(),
                entity.getLastModifiedByUser(),
                entity.getLastAccessedByUser(),
                entity.getWorkspaces().stream().map(WorkspaceMapper::mapToSimpleWorkspaceDto).collect(Collectors.toSet())
        );
    }

}