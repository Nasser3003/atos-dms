package net.atos.mapper;

import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.model.WorkspaceEntity;

public class WorkspaceMapper {

    public static WorkspaceReadDto mapToReadWorkspace (WorkspaceEntity entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity cannot be null");

        return new WorkspaceReadDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getDateOfCreation(),
                entity.getCreatedByUserId(),
                entity.getDocuments(),
                entity.getAccessibleByUsers()
        );
    }

}