package net.atos.controller;

import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.service.document.DocumentAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/document")
public class DocumentAdminController extends AbstractBaseDocumentController {

    @Autowired
    public DocumentAdminController(DocumentAdminService documentService) {
        super(documentService);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DocumentReadOnlyDto>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentReadOnlyDto> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.getDocument(id));
    }

    @PutMapping("/update")
    public ResponseEntity<DocumentReadOnlyDto> updateDocument(@RequestBody DocumentEditDto documentEditDto) {
        return ResponseEntity.ok(documentService.updateDocument(documentEditDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {
        return null;
    }
}