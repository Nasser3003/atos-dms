package net.atos.service.workspace;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.workspace.WorkspaceCreateDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.mapper.WorkspaceMapper;
import net.atos.model.WorkspaceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractWorkspaceService implements IWorkspaceService {

    @Override
    public WorkspaceReadDto createWorkspace(WorkspaceCreateDto createDto) {
        WorkspaceEntity workspaceEntity = new WorkspaceEntity(createDto.getName(), createDto.getDescription(),
                CustomJwtAuthenticationConverter.extractUserIdFromContext());
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
     }

    List<WorkspaceCreateDto> getAllWorkspaces();

    WorkspaceReadDto getWorkspace(UUID id);

    WorkspaceReadDto updateWorkspace(DocumentEditDto documentEditDto);

    void deleteWorkspace(UUID id);

}
