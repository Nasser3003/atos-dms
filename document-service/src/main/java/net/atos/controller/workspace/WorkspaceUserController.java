package net.atos.controller.workspace;

import net.atos.dto.workspace.WorkspaceDocumentDto;
import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.dto.workspace.WorkspaceUserDto;
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
    WorkspaceUserController(WorkspaceUserService workspaceUserService) {
        super(workspaceUserService);
    }

    @Override
    @GetMapping("/{id}")
    ResponseEntity<WorkspaceReadDto> getWorkspace(@PathVariable UUID id) {
        return ResponseEntity.ok(workspaceService.getWorkspace(id));
    }

    @Override
    @GetMapping("/all")
    ResponseEntity<List<WorkspaceReadDto>> getAllWorkspace() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }

    @Override
    @PutMapping("/update")
    ResponseEntity<WorkspaceReadDto> updateWorkspace(@RequestBody WorkspaceEditDto workspaceEditDto) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(workspaceEditDto));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteWorkspace(@PathVariable UUID id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();    }

    @Override
    @PutMapping("/add/doc")
    ResponseEntity<WorkspaceReadDto> addDocumentToWorkspace(@RequestBody WorkspaceDocumentDto workspaceDocumentDto) {
        return ResponseEntity.ok(workspaceService.addDocument(workspaceDocumentDto));
    }

    @Override
    @PutMapping("/rm/doc")
    ResponseEntity<WorkspaceReadDto> removeDocumentFromWorkspace(@RequestBody WorkspaceDocumentDto workspaceDocumentDto) {
        return ResponseEntity.ok(workspaceService.removeDocument(workspaceDocumentDto));
    }

    @Override
    @PutMapping("/add/user")
    ResponseEntity<WorkspaceReadDto> addUserToWorkspace(@RequestBody WorkspaceUserDto workspaceUserDto) {
        return ResponseEntity.ok(workspaceService.addUser(workspaceUserDto));
    }

    @Override
    @PutMapping("/rm/user")
    ResponseEntity<WorkspaceReadDto> removeUserFromWorkspace(@RequestBody WorkspaceUserDto workspaceUserDto) {
        return ResponseEntity.ok(workspaceService.removeUser(workspaceUserDto));
    }
}
