package net.atos.controller;

import lombok.RequiredArgsConstructor;
import net.atos.dto.DocumentDto;
import net.atos.service.DocumentAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class DocumentAdminController {

    private final DocumentAdminService documentAdminService;

    @GetMapping("/documents")
    public ResponseEntity<List<DocumentDto>> getAllDocuments() {
        return ResponseEntity.ok(documentAdminService.getAllDocuments());
    }

    @PostMapping
    public ResponseEntity<DocumentDto> createDocument(@RequestBody DocumentDto documentDto) {
        return ResponseEntity.ok(documentAdminService.createDocument(documentDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDto> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(documentAdminService.getDocument(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<DocumentDto> updateDocument(@PathVariable UUID id, @RequestBody DocumentDto documentDto) {
        return ResponseEntity.ok(documentAdminService.updateDocument(id, documentDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentAdminService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}