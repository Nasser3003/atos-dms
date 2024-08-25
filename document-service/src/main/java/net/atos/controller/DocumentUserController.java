package net.atos.controller;

import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import net.atos.service.DocumentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/user")
public class DocumentUserController extends AbstractBaseDocumentController{

    @Autowired
    public DocumentUserController(DocumentUserService documentService) {
        super(documentService);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID id) {
        return documentService.downloadDocument(id);
    }

    @GetMapping("/documents")
    public ResponseEntity<List<DocumentReadOnlyDto>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/document/{id}")
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
}
