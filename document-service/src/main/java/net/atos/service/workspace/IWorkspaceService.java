package net.atos.service.workspace;

import net.atos.dto.workspace.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IWorkspaceService {

    WorkspaceReadDto createWorkspace(WorkspaceCreateDto createDto);

    WorkspaceReadDto addDocument(WorkspaceDocumentDto workspaceDocumentDto);

    WorkspaceReadDto removeDocument(WorkspaceDocumentDto workspaceDocumentDto);

    List<WorkspaceReadDto> getAllWorkspaces();

    WorkspaceReadDto getWorkspace(UUID id);

    WorkspaceReadDto updateWorkspace(WorkspaceEditDto workspaceEditDto);

    void deleteWorkspace(UUID id);

    ResponseEntity<Resource> downloadDocument(UUID id);

    WorkspaceReadDto addUser(WorkspaceUserDto workspaceUserDto);

    WorkspaceReadDto removeUser(WorkspaceUserDto workspaceUserDto);
}


