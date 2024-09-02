package net.atos.controller.search;

import lombok.RequiredArgsConstructor;
import net.atos.model.DocumentEntity;
import net.atos.model.enums.EnumDataType;
import net.atos.service.search.AdminSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin/documents/search")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminSearchApi {
    private final AdminSearchService searchService;

    @GetMapping("/type")
    public ResponseEntity<List<DocumentEntity>> searchByType(@RequestParam String type) {
        return ResponseEntity.ok(searchService.searchDocumentsByType(EnumDataType.valueOf(type)));
    }

    @GetMapping("/name")
    public ResponseEntity<List<DocumentEntity>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(searchService.searchDocumentsByName(name));
    }

    @GetMapping("/tag")
    public ResponseEntity<List<DocumentEntity>> searchByTag(@RequestParam String tag) {
        return ResponseEntity.ok(searchService.searchDocumentsByTag(tag));
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<DocumentEntity>> searchIsDeleted(@RequestParam(name = "deleted") boolean deleted) {
        return ResponseEntity.ok(searchService.searchIsDeleted(deleted));
    }
}
