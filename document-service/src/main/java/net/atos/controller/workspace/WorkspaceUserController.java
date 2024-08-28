package net.atos.controller.workspace;

import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.service.workspace.WorkspaceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceReadDto> getWorkspace(@PathVariable UUID id) {
        return ResponseEntity.ok(workspaceService.getWorkspace(id));
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<WorkspaceReadDto>> getAllWorkspace() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }

    @Override
    @PutMapping("/update")
    public ResponseEntity<WorkspaceReadDto> updateWorkspace(@RequestBody WorkspaceEditDto workspaceEditDto) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(workspaceEditDto));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable UUID id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();    }
}
