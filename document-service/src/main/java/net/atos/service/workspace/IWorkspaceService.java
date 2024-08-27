package net.atos.service.workspace;

import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.workspace.WorkspaceCreateDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IWorkspaceService {

    WorkspaceReadDto createWorkspace(WorkspaceCreateDto createDto);

    List<WorkspaceCreateDto> getAllWorkspaces();

    WorkspaceReadDto getWorkspace(UUID id);

    WorkspaceReadDto updateWorkspace(DocumentEditDto documentEditDto);

    void deleteWorkspace(UUID id);

    ResponseEntity<Resource> downloadDocument(UUID id);
}


