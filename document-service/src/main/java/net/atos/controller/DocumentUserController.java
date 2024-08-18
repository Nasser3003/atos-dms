package net.atos.controller;

import lombok.RequiredArgsConstructor;
import net.atos.dto.DocumentCreateDto;
import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import net.atos.service.DocumentAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/user")
@RequiredArgsConstructor
public class DocumentUserController {

    private final DocumentAdminService documentAdminService;
    private static final Logger logger = LoggerFactory.getLogger(DocumentUserController.class);

    @PostMapping("/document")
    public ResponseEntity<DocumentReadOnlyDto> createDocument(@RequestBody DocumentCreateDto documentCreateDto) {
        logger.info("Received request to create document: {}", documentCreateDto);
        try {
            DocumentReadOnlyDto result = documentAdminService.createDocument(documentCreateDto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error creating document", e);
            throw e;
        }
    }

    @GetMapping("/documents")
    public ResponseEntity<List<DocumentReadOnlyDto>> getAllDocuments() {
        return ResponseEntity.ok(documentAdminService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentReadOnlyDto> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(documentAdminService.getDocument(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentReadOnlyDto> updateDocument(@RequestBody DocumentEditDto documentEditDto) {
        return ResponseEntity.ok(documentAdminService.updateDocument(documentEditDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentAdminService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}