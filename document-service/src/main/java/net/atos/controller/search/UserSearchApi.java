package net.atos.controller.search;

import lombok.RequiredArgsConstructor;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.mapper.DocumentMapper;
import net.atos.service.search.UserSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/documents/search")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserSearchApi {
    private final UserSearchService searchService;

    @GetMapping
    public ResponseEntity<Set<DocumentReadOnlyDto>> searchAny(@RequestParam String query) {
        return ResponseEntity.ok(searchService.search(query)
                .stream().map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toSet()));
    }

}
