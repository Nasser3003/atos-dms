package net.atos.controller;

import lombok.RequiredArgsConstructor;
import net.atos.dto.DocumentCreateDto;
import net.atos.dto.DocumentEditDto;
import net.atos.dto.DocumentReadOnlyDto;
import net.atos.model.enums.EnumDataType;
import net.atos.service.IDocumentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractBaseDocumentController {

    protected final IDocumentService documentService;

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
        return ResponseEntity.ok(documentService.createDocument(documentCreateDto));
    }

    @GetMapping("/document/{id}")
    public abstract ResponseEntity<DocumentReadOnlyDto> getDocument(@PathVariable UUID id);

    @PutMapping("/update")
    public abstract ResponseEntity<DocumentReadOnlyDto> updateDocument(@RequestBody DocumentEditDto documentEditDto);

    @DeleteMapping("/delete/{id}")
    public abstract ResponseEntity<Void> deleteDocument(@PathVariable UUID id);
}