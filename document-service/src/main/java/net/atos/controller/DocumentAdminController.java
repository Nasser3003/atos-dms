package net.atos.controller;

import lombok.RequiredArgsConstructor;
import net.atos.dto.DocumentCreateDto;
import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import net.atos.service.DocumentAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class DocumentAdminController {

    private final DocumentAdminService documentAdminService;

    @PostMapping("/document")
    public ResponseEntity<DocumentReadOnlyDto> createDocument(@RequestBody DocumentCreateDto documentCreateDto) {
        return ResponseEntity.ok(documentAdminService.createDocument(documentCreateDto));
    }

    @GetMapping("/documents")
    public ResponseEntity<List<DocumentReadOnlyDto>> getAllDocuments() {
        return ResponseEntity.ok(documentAdminService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentReadOnlyDto> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(documentAdminService.getDocument(id));
    }

    @PutMapping
    public ResponseEntity<DocumentReadOnlyDto> updateDocument(@RequestBody DocumentEditDto documentEditDto) {
        return ResponseEntity.ok(documentAdminService.updateDocument(documentEditDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentAdminService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}