package net.atos.controller.workspace;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.workspace.WorkspaceCreateDto;
import net.atos.dto.workspace.WorkspaceDocumentDto;
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
    public ResponseEntity<WorkspaceReadDto> createWorkspace(@RequestBody WorkspaceCreateDto workspaceCreateDto) {
        return ResponseEntity.ok(workspaceService.createWorkspace(workspaceCreateDto));
    }

    public abstract ResponseEntity<WorkspaceReadDto> getWorkspace(@PathVariable UUID id);

    public abstract ResponseEntity<List<WorkspaceReadDto>> getAllWorkspace();

    public abstract ResponseEntity<WorkspaceReadDto> updateWorkspace(@RequestBody WorkspaceEditDto workspaceEditDto);

    public abstract ResponseEntity<Void> deleteWorkspace(@PathVariable UUID id);

    public abstract ResponseEntity<WorkspaceReadDto> addDocumentToWorkspace(@RequestBody WorkspaceDocumentDto workspaceDocumentDto);

    public abstract ResponseEntity<WorkspaceReadDto> removeDocumentFromWorkspace(@RequestBody WorkspaceDocumentDto workspaceDocumentDto);
}