package net.atos.service.workspace;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.workspace.WorkspaceCreateDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.exception.NotFoundException;
import net.atos.mapper.WorkspaceMapper;
import net.atos.model.WorkspaceEntity;
import net.atos.repository.DocumentRepository;
import net.atos.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractWorkspaceService implements IWorkspaceService {

    final WorkspaceRepository repository;
    final DocumentRepository documentRepository;

    @Override
    @Transactional
    public WorkspaceReadDto createWorkspace(WorkspaceCreateDto createDto) {
        WorkspaceEntity workspaceEntity = new WorkspaceEntity(createDto.getName(), createDto.getDescription(),
                CustomJwtAuthenticationConverter.extractUserIdFromContext());
        repository.save(workspaceEntity);
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
     }

    boolean NotWorkspaceOwner(WorkspaceEntity workspaceEntity) {
        UUID userId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        return !workspaceEntity.getCreatedByUserId().equals(userId);
    }

    WorkspaceEntity findNoneDeletedWorkspace(UUID id) {
        WorkspaceEntity workspaceEntity = findWorkspaceById(id);
        if (workspaceEntity.isDeleted())
            throw new NotFoundException("Workspace doesnt exist or is deleted");
        return workspaceEntity;
    }

    private WorkspaceEntity findWorkspaceById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workspace with id " + id + " not found"));

    }

}
