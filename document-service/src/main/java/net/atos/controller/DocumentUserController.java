package net.atos.controller;

import lombok.RequiredArgsConstructor;
import net.atos.dto.DocumentCreateDto;
import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import net.atos.model.enums.EnumDataType;
import net.atos.service.DocumentUserService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/user")
@RequiredArgsConstructor
public class DocumentUserController {

    private final DocumentUserService documentUserService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentReadOnlyDto> createDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart("type") EnumDataType type,
            @RequestPart(value = "filePath", required = false) String filePath) {

        DocumentCreateDto documentCreateDto = new DocumentCreateDto(
                filePath != null ? filePath : file.getOriginalFilename(),
                type,
                file.getSize(),
                file
        );
        return ResponseEntity.ok(documentUserService.createDocument(documentCreateDto));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID id) throws IOException {
        return documentUserService.downloadDocument(id);
    }

    @GetMapping("/documents")
    public ResponseEntity<List<DocumentReadOnlyDto>> getAllDocuments() {
        return ResponseEntity.ok(documentUserService.getAllDocuments());
    }

    @GetMapping("/document/{id}")
    public ResponseEntity<DocumentReadOnlyDto> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(documentUserService.getDocument(id));
    }

    @PutMapping("/update")
    public ResponseEntity<DocumentReadOnlyDto> updateDocument(@RequestBody DocumentEditDto documentEditDto) {
        return ResponseEntity.ok(documentUserService.updateDocument(documentEditDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentUserService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
