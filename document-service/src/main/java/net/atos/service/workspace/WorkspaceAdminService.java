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
public class WorkspaceAdminService extends AbstractWorkspaceService {

    @Autowired
    public WorkspaceAdminService(WorkspaceRepository repository) {
        super(repository);
    }

    @Override
    public List<WorkspaceReadDto> getAllWorkspaces() {
        return repository.findAll().stream()
                .map(WorkspaceMapper::mapToReadWorkspace)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceReadDto getWorkspace(UUID id) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
    }

    @Override
    public WorkspaceReadDto updateWorkspace(WorkspaceEditDto workspaceEditDto) {
        return null;
    }

    @Override
    public void deleteWorkspace(UUID id) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        workspaceEntity.setDeleted(true);
        repository.save(workspaceEntity);
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {
        return null;
    }
}
