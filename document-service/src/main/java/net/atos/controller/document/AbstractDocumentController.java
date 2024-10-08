package net.atos.controller.document;

import lombok.RequiredArgsConstructor;
import net.atos.dto.document.DocumentCreateDto;
import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.dto.document.DocumentUserDto;
import net.atos.model.enums.EnumDataType;
import net.atos.service.document.IDocumentService;
import net.atos.service.document.PreviewFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractDocumentController {

    final IDocumentService documentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<DocumentReadOnlyDto> createDocument(
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "type") String type,
            @RequestPart(value = "filePath", required = false) String filePath) {

        DocumentCreateDto documentCreateDto = new DocumentCreateDto(
                filePath != null ? filePath : file.getOriginalFilename(),
                EnumDataType.valueOf(type),
                file.getSize(),
                file
        );
        return ResponseEntity.ok(documentService.createDocument(documentCreateDto));
    }


    abstract ResponseEntity<List<DocumentReadOnlyDto>> getAllDocuments();

    abstract ResponseEntity<List<DocumentReadOnlyDto>> getALlDeletedDocuments();

    abstract ResponseEntity<DocumentReadOnlyDto> getDocument(@PathVariable UUID id);

    abstract ResponseEntity<DocumentReadOnlyDto> updateDocument(@RequestBody DocumentEditDto documentEditDto);

    abstract ResponseEntity<Void> deleteDocument(@PathVariable UUID id);

    abstract ResponseEntity<Void> undeleteDocument(@PathVariable UUID id);

    abstract ResponseEntity<Resource> downloadDocument(@PathVariable UUID id);

    abstract ResponseEntity<PreviewFileResponse> previewFile(@PathVariable UUID id);

    abstract ResponseEntity<DocumentReadOnlyDto> addUserToDocument(@RequestBody DocumentUserDto documentUserDto);

    abstract ResponseEntity<DocumentReadOnlyDto> removeUserToDocument(@RequestBody DocumentUserDto documentUserDto);

}