package net.atos.service.workspace;

import lombok.RequiredArgsConstructor;
import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.workspace.WorkspaceDocumentDto;
import net.atos.dto.workspace.WorkspaceCreateDto;
import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
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

    WorkspaceEntity findNoneDeletedWorkspace(UUID id) {
        WorkspaceEntity workspaceEntity = findWorkspaceById(id);
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
        return workspaceEntity;
    }

    WorkspaceEntity removeDocumentHelper(WorkspaceDocumentDto workspaceDocumentDto) {
        WorkspaceEntity  workspaceEntity= findNoneDeletedWorkspace(workspaceDocumentDto.getWorkspaceId());
        DocumentEntity documentEntity = documentRepository.findById(workspaceDocumentDto.getDocumentId())
                .orElseThrow(() -> new FileStorageException("document not found"));

        workspaceEntity.removeDocument(documentEntity);
        repository.save(workspaceEntity);
        return workspaceEntity;
    }

    private WorkspaceEntity findWorkspaceById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Workspace with id " + id + " not found"));

    }

}
