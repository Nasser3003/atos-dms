package net.atos.controller.workspace;

import lombok.RequiredArgsConstructor;
import net.atos.dto.workspace.WorkspaceCreateDto;
import net.atos.dto.workspace.WorkspaceDocumentDto;
import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.service.workspace.IWorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractWorkspaceController {

    final IWorkspaceService workspaceService;

    @PostMapping("/create")
    public ResponseEntity<WorkspaceReadDto> createWorkspace(@RequestBody WorkspaceCreateDto workspaceCreateDto) {
        return ResponseEntity.ok(workspaceService.createWorkspace(workspaceCreateDto));
    }

    abstract ResponseEntity<WorkspaceReadDto> getWorkspace(@PathVariable UUID id);

    abstract ResponseEntity<List<WorkspaceReadDto>> getAllWorkspace();

    abstract ResponseEntity<WorkspaceReadDto> updateWorkspace(@RequestBody WorkspaceEditDto workspaceEditDto);

    abstract ResponseEntity<Void> deleteWorkspace(@PathVariable UUID id);

    abstract ResponseEntity<WorkspaceReadDto> addDocumentToWorkspace(@RequestBody WorkspaceDocumentDto workspaceDocumentDto);

    abstract ResponseEntity<WorkspaceReadDto> removeDocumentFromWorkspace(@RequestBody WorkspaceDocumentDto workspaceDocumentDto);

    abstract ResponseEntity<WorkspaceReadDto> addUserToWorkspace(@RequestBody WorkspaceDocumentDto workspaceDocumentDto);

    abstract ResponseEntity<WorkspaceReadDto> removeUserFromWorkspace(@RequestBody WorkspaceDocumentDto workspaceDocumentDto);

}