package net.atos.controller.document;

import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.dto.document.DocumentUserDto;
import net.atos.service.document.DocumentAdminService;
import net.atos.service.document.FileDownloadInfo;
import net.atos.service.document.PreviewFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/document")
public class DocumentAdminController extends AbstractDocumentController {

    @Autowired
    DocumentAdminController(DocumentAdminService documentService) {
        super(documentService);
    }

    @Override
    @GetMapping("/all")
    ResponseEntity<List<DocumentReadOnlyDto>> getAllDocuments() {
            return ResponseEntity.ok(documentService.getAllUserDocuments());
    }

    @Override
    @GetMapping("/deleted/all")
    ResponseEntity<List<DocumentReadOnlyDto>> getALlDeletedDocuments() {
        return ResponseEntity.ok(documentService.getAllDeletedDocuments());
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
    @PutMapping("/undelete/{id}")
    ResponseEntity<Void> undeleteDocument(@PathVariable UUID id) {
        documentService.undeleteDocument(id);
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
    @GetMapping("/preview/{id}")
    public ResponseEntity<PreviewFileResponse> previewFile(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.previewDocument(id));
    }

    @Override
    @PutMapping("/add/user")
    ResponseEntity<DocumentReadOnlyDto> addUserToDocument(@RequestBody DocumentUserDto documentUserDto) {
        return ResponseEntity.ok(documentService.addUser(documentUserDto));
    }

    @Override
    @PutMapping ("/rm/user")
    ResponseEntity<DocumentReadOnlyDto> removeUserToDocument(@RequestBody DocumentUserDto documentUserDto) {
        return ResponseEntity.ok(documentService.removeUser(documentUserDto));
    }
}