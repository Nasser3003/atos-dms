package net.atos.controller.workspace;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.workspace.WorkspaceCreateDto;
import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.mapper.WorkspaceMapper;
import net.atos.model.WorkspaceEntity;
import net.atos.service.workspace.IWorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractWorkspaceController {

    final IWorkspaceService workspaceService;

    @PostMapping("/create")
    public ResponseEntity<WorkspaceReadDto> createWorkspace(WorkspaceCreateDto workspaceCreateDto) {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        WorkspaceEntity workspaceEntity = new WorkspaceEntity(workspaceCreateDto.getName(),
                workspaceCreateDto.getDescription(), authenticatedUserId);
        return ResponseEntity.ok(WorkspaceMapper.mapToReadWorkspace(workspaceEntity));
    }

    @GetMapping("/workspace/{id}")
    public abstract ResponseEntity<WorkspaceReadDto> getWorkspace(@PathVariable UUID id);

    @GetMapping("/workspace/all")
    public abstract ResponseEntity<List<WorkspaceReadDto>> getAllWorkspace();

    @PutMapping("/update")
    public abstract ResponseEntity<WorkspaceReadDto> updateWorkspace(@RequestBody WorkspaceEditDto workspaceEditDto);

    @DeleteMapping("/delete/{id}")
    public abstract ResponseEntity<Void> deleteWorkspace(@PathVariable UUID id);
}