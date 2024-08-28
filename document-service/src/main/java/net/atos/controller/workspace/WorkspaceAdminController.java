package net.atos.controller.workspace;

import net.atos.dto.workspace.WorkspaceDocumentDto;
import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.service.workspace.WorkspaceAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping("/add/doc")
    public ResponseEntity<WorkspaceReadDto> addDocumentToWorkspace(@RequestBody WorkspaceDocumentDto workspaceDocumentDto) {
        return ResponseEntity.ok(workspaceService.addDocument(workspaceDocumentDto));
    }

    @Override
    @PutMapping("/rm/doc")
    public ResponseEntity<WorkspaceReadDto> removeDocumentFromWorkspace(@RequestBody WorkspaceDocumentDto workspaceDocumentDto) {
        return ResponseEntity.ok(workspaceService.removeDocument(workspaceDocumentDto));
    }
}
