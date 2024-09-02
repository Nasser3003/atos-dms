package net.atos.service.workspace;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.workspace.*;
import net.atos.exception.FileStorageException;
import net.atos.exception.NotFoundException;
import net.atos.mapper.WorkspaceMapper;
import net.atos.model.DocumentEntity;
import net.atos.model.WorkspaceEntity;
import net.atos.repository.DocumentRepository;
import net.atos.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class AbstractWorkspaceService implements IWorkspaceService {

    final WorkspaceRepository repository;
    final DocumentRepository documentRepository;

    @Override
    @Transactional
    public WorkspaceReadDto createWorkspace(WorkspaceCreateDto createDto) {
        WorkspaceEntity workspaceEntity = new WorkspaceEntity(createDto.getName(), createDto.getDescription(),
                CustomJwtAuthenticationConverter.extractUserIdFromContext());
        repository.save(workspaceEntity);
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
     }

    boolean notWorkspaceOwner(WorkspaceEntity workspaceEntity) {
        UUID userId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        return !workspaceEntity.getCreatedByUserId().equals(userId);
    }

    boolean notWorkspaceMember(WorkspaceEntity workspaceEntity) {
        UUID userId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        return !workspaceEntity.getAccessibleByUsers().contains(userId);
    }

    WorkspaceEntity findNoneDeletedWorkspace(UUID workspaceId) {
        WorkspaceEntity workspaceEntity = findWorkspaceById(workspaceId);
        if (workspaceEntity.isDeleted())
            throw new NotFoundException("Workspace doesnt exist or is deleted");
        return workspaceEntity;
    }

    WorkspaceEntity updateWorkspaceHelper(WorkspaceEditDto workspaceEditDto) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceEditDto.getId());

        if (workspaceEditDto.getName() != null && !workspaceEditDto.getName().isBlank())
            workspaceEntity.setName(workspaceEditDto.getName().trim());
        if (workspaceEditDto.getDescription() != null && !workspaceEditDto.getDescription().isBlank())
            workspaceEntity.setDescription(workspaceEditDto.getDescription().trim());

        repository.save(workspaceEntity);
        return workspaceEntity;
    }

    WorkspaceEntity addDocumentHelper(WorkspaceDocumentDto workspaceDocumentDto) {
        WorkspaceEntity  workspaceEntity= findNoneDeletedWorkspace(workspaceDocumentDto.getWorkspaceId());
        DocumentEntity documentEntity = documentRepository.findById(workspaceDocumentDto.getDocumentId())
                .orElseThrow(() -> new FileStorageException("document not found"));
        if (documentEntity.isDeleted())
            throw new NotFoundException("Document doesnt exist or is deleted");

        workspaceEntity.addDocument(documentEntity);
        repository.save(workspaceEntity);
        documentRepository.save(documentEntity);
        return workspaceEntity;
    }

    WorkspaceEntity removeDocumentHelper(WorkspaceDocumentDto workspaceDocumentDto) {
        WorkspaceEntity  workspaceEntity= findNoneDeletedWorkspace(workspaceDocumentDto.getWorkspaceId());
        DocumentEntity documentEntity = documentRepository.findById(workspaceDocumentDto.getDocumentId())
                .orElseThrow(() -> new FileStorageException("document not found"));

        workspaceEntity.removeDocument(documentEntity);
        repository.save(workspaceEntity);
        documentRepository.save(documentEntity);
        return workspaceEntity;
    }

    private WorkspaceEntity findWorkspaceById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workspace with id " + id + " not found"));

    }

    WorkspaceEntity addUserHelper(WorkspaceUserDto workspaceUserDto) {
        if (workspaceUserDto.getUserId() == null)
            throw new IllegalArgumentException("user not found");
        if (workspaceUserDto.getWorkspaceId() == null)
            throw new IllegalArgumentException("workspace not found");

        UUID userId = workspaceUserDto.getUserId();
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceUserDto.getWorkspaceId());
        workspaceEntity.addUser(userId);
        repository.save(workspaceEntity);
        return workspaceEntity;
    }

    WorkspaceEntity removeUserHelper(WorkspaceUserDto workspaceUserDto) {
        if (workspaceUserDto.getUserId() == null)
            throw new IllegalArgumentException("user not found");
        if (workspaceUserDto.getWorkspaceId() == null)
            throw new IllegalArgumentException("workspace not found");


        UUID userId = workspaceUserDto.getUserId();
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceUserDto.getWorkspaceId());
        workspaceEntity.removeUser(userId);
        repository.save(workspaceEntity);
        return workspaceEntity;
    }

}
