package net.atos.controller.workspace;

import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.service.workspace.WorkspaceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/workspace")
public class WorkspaceUserController extends AbstractWorkspaceController {

    @Autowired
    public WorkspaceUserController(WorkspaceUserService workspaceUserService) {
        super(workspaceUserService);
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
        workspaceService.updateWorkspace(workspaceEditDto);
        // TODO
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteWorkspace(UUID id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();    }
}
