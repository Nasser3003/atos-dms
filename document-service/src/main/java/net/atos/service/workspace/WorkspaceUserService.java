package net.atos.service.workspace;

import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.workspace.WorkspaceCreateDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public class WorkspaceUserService extends AbstractWorkspaceService {
    @Override
    public List<WorkspaceCreateDto> getAllWorkspaces() {
        return List.of();
    }

    @Override
    public WorkspaceReadDto getWorkspace(UUID id) {
        return null;
    }

    @Override
    public WorkspaceReadDto updateWorkspace(DocumentEditDto documentEditDto) {
        return null;
    }

    @Override
    public void deleteWorkspace(UUID id) {

    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {
        return null;
    }
}
