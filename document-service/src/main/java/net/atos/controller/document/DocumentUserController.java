package net.atos.controller.document;

import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.service.document.DocumentUserService;
import net.atos.service.document.FileDownloadInfo;
import net.atos.service.document.PreviewFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/document")
public class DocumentUserController extends AbstractDocumentController {

    @Autowired
    DocumentUserController(DocumentUserService documentService) {
        super(documentService);
    }

    @Override
    @GetMapping("/all")
    ResponseEntity<List<DocumentReadOnlyDto>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @Override
    @GetMapping("/{id}")
    ResponseEntity<DocumentReadOnlyDto> getDocument(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.getDocument(id));
    }

    @Override
    @PutMapping("/update")
    ResponseEntity<DocumentReadOnlyDto> updateDocument(@RequestBody DocumentEditDto documentEditDto) {
        return ResponseEntity.ok(documentService.updateDocument(documentEditDto));
    }

    @Override
    @DeleteMapping("/delete/{id}")
    ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/download/{id}")
    ResponseEntity<Resource> downloadDocument(@PathVariable UUID id) {
        FileDownloadInfo fileInfo = documentService.downloadDocument(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileInfo.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(fileInfo.getContentType()))
                .contentLength(fileInfo.getContentLength())
                .body(fileInfo.getResource());
    }

    @Override
    ResponseEntity<PreviewFileResponse> previewFile(UUID id) {
        return ResponseEntity.ok(documentService.previewDocument(id));
    }
}
