package net.atos.service.document;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.document.DocumentCreateDto;
import net.atos.dto.document.DocumentEditDto;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.exception.NotFoundException;
import net.atos.mapper.DocumentMapper;
import net.atos.model.DocumentEntity;
import net.atos.repository.DocumentRepository;
import net.atos.service.LocalFileStorageService;
import net.atos.util.LocalFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static net.atos.util.LocalFileUtil.extractFileName;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractDocumentService implements IDocumentService {

    final DocumentRepository repository;
    final LocalFileStorageService fileStorageService;

    @Override
    @Transactional
    public DocumentReadOnlyDto createDocument(DocumentCreateDto createDto) {
        UUID userId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        String sanitizedFileName = LocalFileUtil.sanitizeFileName(Objects.requireNonNull(extractFileName(createDto.getFilePath())));

        DocumentEntity documentEntity = new DocumentEntity(
                sanitizedFileName,
                createDto.getType(),
                createDto.getSizeInBytes(),
                userId
        );

        fileStorageService.storeFile(createDto.getFile(), sanitizedFileName);
        repository.save(documentEntity);

        return DocumentMapper.mapToReadDocument(documentEntity);
    }

    boolean NotFileOwner(DocumentEntity document) {
        UUID userId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        return !document.getCreatedByUserId().equals(userId);
    }

    DocumentReadOnlyDto updateDocumentHelper(DocumentEditDto documentEditDto) {
        if (documentEditDto == null)
            throw new IllegalArgumentException("Entity cannot be null");

        DocumentEntity entity = findNoneDeletedDocumentById(documentEditDto.getId());

        entity.setLastAccessedByUserId(CustomJwtAuthenticationConverter.extractUserIdFromContext());
        entity.setLastAccessed(LocalDateTime.now());
        String oldPath = findNoneDeletedDocumentById(documentEditDto.getId()).getFilePath();

        if (documentEditDto.getFilePath() != null && !documentEditDto.getFilePath().isBlank())
            if (!oldPath.equals(documentEditDto.getFilePath()))
                fileStorageService.renameFile(oldPath, documentEditDto.getFilePath());

        if (documentEditDto.getFilePath() != null)
            entity.setFilePath(fileStorageService.baseStorageLocation
                    .resolve(CustomJwtAuthenticationConverter.extractUserIdFromContext().toString())
                    .resolve(documentEditDto.getFilePath()).toString());
        if (documentEditDto.getType() != null)
            entity.setType(documentEditDto.getType());
        if (documentEditDto.getTags() != null)
            entity.setTags(documentEditDto.getTags());
        if (documentEditDto.getIsPublic() != null)
            entity.setPublic(documentEditDto.getIsPublic());

        repository.save(entity);

        return DocumentMapper.mapToReadDocument(entity);
    }

    DocumentEntity findNoneDeletedDocumentById(UUID id) {
        DocumentEntity documentEntity = findDocumentById(id);
        if (documentEntity.isDeleted())
            throw new NotFoundException("file doesnt exist or is deleted");
        return documentEntity;
    }

    FileDownloadInfo downloadFileHelper(UUID documentId) throws IOException {
        Path filePath = fileStorageService.getFilePathById(documentId);

        if (!Files.exists(filePath) || !Files.isReadable(filePath))
            throw new NotFoundException("File not found or not readable: " + filePath);

        String fileName = filePath.getFileName().toString();
        String contentType = Files.probeContentType(filePath);
        contentType = (contentType != null) ? contentType : "application/octet-stream";
        long contentLength = Files.size(filePath);

        Resource resource = new InputStreamResource(Files.newInputStream(filePath));

        return new FileDownloadInfo(resource, fileName, contentType, contentLength);
    }

    private DocumentEntity findDocumentById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Document with id " + id + " not found"));

    }

}