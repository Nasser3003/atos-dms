package net.atos.controller.workspace;

import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.service.workspace.WorkspaceAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/workspace")
public class WorkspaceAdminController extends AbstractWorkspaceController{

    @Autowired
    public WorkspaceAdminController(WorkspaceAdminService workspaceAdminService) {
        super(workspaceAdminService);
    }

    @Override
    public ResponseEntity<WorkspaceReadDto> getWorkspace(UUID id) {
        return ResponseEntity.ok(workspaceService.getWorkspace(id));
    }

    @Override
    public ResponseEntity<List<WorkspaceReadDto>> getAllWorkspace() {
       return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }

    @Override
    public ResponseEntity<WorkspaceReadDto> updateWorkspace(WorkspaceEditDto workspaceEditDto) {
        // TODO
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteWorkspace(UUID id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();
    }
}
