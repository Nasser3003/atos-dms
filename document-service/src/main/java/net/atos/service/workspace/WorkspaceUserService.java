package net.atos.service.workspace;

import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.exception.NotFoundException;
import net.atos.exception.UnauthorizedException;
import net.atos.mapper.WorkspaceMapper;
import net.atos.model.DocumentEntity;
import net.atos.model.WorkspaceEntity;
import net.atos.repository.DocumentRepository;
import net.atos.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkspaceUserService extends AbstractWorkspaceService {

    @Autowired
    public WorkspaceUserService(WorkspaceRepository repository, DocumentRepository documentRepository) {
        super(repository, documentRepository);
    }

    @Override
    @Transactional
    public WorkspaceReadDto addDocument(UUID documentId, UUID workspaceId) {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceId);
        DocumentEntity documentEntity = documentRepository.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document with ID " + documentId + " not found"));

        if (!workspaceEntity.getCreatedByUserId().equals(authenticatedUserId))
            throw new UnauthorizedException("You are not authorized to add documents to this workspace");

        if (!documentEntity.getCreatedByUserId().equals(authenticatedUserId))
            throw new UnauthorizedException("You are not authorized to add this document to a workspace");

        workspaceEntity.addDocument(documentEntity);
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
    }

    @Override
    @Transactional
    public WorkspaceReadDto removeDocument(UUID documentId, UUID workspaceId) {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceId);
        DocumentEntity documentEntity = documentRepository.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document with ID " + documentId + " not found"));

        if (!workspaceEntity.getCreatedByUserId().equals(authenticatedUserId))
            throw new UnauthorizedException("You are not authorized to remove documents to this workspace");

        if (!documentEntity.getCreatedByUserId().equals(authenticatedUserId))
            throw new UnauthorizedException("You are not authorized to remove this document from a workspace");

        workspaceEntity.removeDocument(documentEntity);
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
    }

    @Override
    public List<WorkspaceReadDto> getAllWorkspaces() {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        return repository.findAll().stream()
                .filter(workspaceEntity -> workspaceEntity.isUserAuthorized(authenticatedUserId))
                .map(WorkspaceMapper::mapToReadWorkspace)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceReadDto getWorkspace(UUID id) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        if (!workspaceEntity.isUserAuthorized(authenticatedUserId))
            throw new UnauthorizedException("you dont have permissions to access this workspace");
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
    }

    @Override
    @Transactional
    public WorkspaceReadDto updateWorkspace(WorkspaceEditDto workspaceEditDto) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceEditDto.getId());
        if (NotWorkspaceOwner(workspaceEntity))
            throw new UnauthorizedException("You are not authorized to update this workspace");

        return WorkspaceMapper.mapToReadWorkspace(updateWorkspaceHelper(workspaceEditDto));
    }

    @Override
    public void deleteWorkspace(UUID id) {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        if (workspaceEntity.getCreatedByUserId().equals(authenticatedUserId))
            throw new UnauthorizedException("you dont have permissions to delete this workspace");
        workspaceEntity.setDeleted(true);
        repository.save(workspaceEntity);
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {
        return null;
    }
}
