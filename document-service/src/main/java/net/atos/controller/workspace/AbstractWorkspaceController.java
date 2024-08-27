package net.atos.controller.workspace;

import lombok.RequiredArgsConstructor;
import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.service.workspace.IWorkspaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractWorkspaceController {

    protected final IWorkspaceService workspaceService;

    @PostMapping("/create")
    public abstract ResponseEntity<DocumentReadOnlyDto> createWorkspace(@PathVariable UUID id);

    @GetMapping("/workspace/{id}")
    public abstract ResponseEntity<DocumentReadOnlyDto> getWorkspace(@PathVariable UUID id);

    @PutMapping("/update")
    public abstract ResponseEntity<DocumentReadOnlyDto> updateWorkspace(@RequestBody DocumentEditDto documentEditDto);

    @DeleteMapping("/delete/{id}")
    public abstract ResponseEntity<Void> deleteWorkspace(@PathVariable UUID id);
}