package net.atos.controller;

import lombok.RequiredArgsConstructor;
import net.atos.dto.DocumentCreateDto;
import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import net.atos.model.enums.EnumDataType;
import net.atos.service.DocumentAdminService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class DocumentAdminController {

    private final DocumentAdminService documentAdminService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentReadOnlyDto> createDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "type", required = false) String type,
            @RequestPart(value = "filePath", required = false) String filePath) {

        DocumentCreateDto documentCreateDto = new DocumentCreateDto(
                filePath != null ? filePath : file.getOriginalFilename(),
                EnumDataType.valueOf(type),
                file.getSize(),
                file
        );
        return ResponseEntity.ok(documentAdminService.createDocument(documentCreateDto));
    }


    @GetMapping("/documents")
    public ResponseEntity<List<DocumentReadOnlyDto>> getAllDocuments() {
        return ResponseEntity.ok(documentAdminService.getAllDocuments());
    }

    @GetMapping("/document/{id}")
    public ResponseEntity<DocumentReadOnlyDto> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(documentAdminService.getDocument(id));
    }

    @PutMapping("/update")
    public ResponseEntity<DocumentReadOnlyDto> updateDocument(@RequestBody DocumentEditDto documentEditDto) {
        return ResponseEntity.ok(documentAdminService.updateDocument(documentEditDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentAdminService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}