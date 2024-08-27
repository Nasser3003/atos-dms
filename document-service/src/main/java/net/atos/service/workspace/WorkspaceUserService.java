package net.atos.service.workspace;

import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.exception.UnauthorizedException;
import net.atos.mapper.WorkspaceMapper;
import net.atos.model.WorkspaceEntity;
import net.atos.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkspaceUserService extends AbstractWorkspaceService {

    @Autowired
    public WorkspaceUserService(WorkspaceRepository repository) {
        super(repository);
    }

    @Override
    public List<WorkspaceReadDto> getAllWorkspaces() {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        return repository.findAll().stream()
                .filter(workspaceEntity -> !workspaceEntity.isUserUnauthorized(authenticatedUserId))
                .map(WorkspaceMapper::mapToReadWorkspace)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceReadDto getWorkspace(UUID id) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        if (workspaceEntity.isUserUnauthorized(authenticatedUserId))
            throw new UnauthorizedException("you dont have permissions to access this workspace");
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
    }

    @Override
    public WorkspaceReadDto updateWorkspace(WorkspaceEditDto workspaceEditDto) {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceEditDto.getId());
        if (workspaceEntity.getCreatedByUserId().equals(authenticatedUserId))
            throw new UnauthorizedException("you dont have permissions to update this workspace");

        // use update helper and persist the changes
        // TODO
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
    }

    @Override
    public void deleteWorkspace(UUID id) {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        if (workspaceEntity.getCreatedByUserId().equals(authenticatedUserId))
            throw new UnauthorizedException("you dont have permissions to delete this workspace");
        workspaceEntity.setDeleted(true);
        repository.save(workspaceEntity);
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {
        return null;
    }
}
