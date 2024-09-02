package net.atos.service.workspace;

import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.dto.workspace.WorkspaceDocumentDto;
import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.dto.workspace.WorkspaceUserDto;
import net.atos.mapper.DocumentMapper;
import net.atos.mapper.WorkspaceMapper;
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
public class WorkspaceAdminService extends AbstractWorkspaceService {

    @Autowired
    public WorkspaceAdminService(WorkspaceRepository repository, DocumentRepository documentRepository) {
        super(repository, documentRepository);
    }

    @Override
    public List<WorkspaceReadDto> getAllWorkspaces() {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        return getAllWorkspaces(authenticatedUserId);
    }

    @Override
    public List<WorkspaceReadDto> getAllWorkspaces(UUID userId) {
        return repository.findAll().stream()
                .filter(workspaceEntity -> workspaceEntity.isUserAuthorized(userId))
                .map(WorkspaceMapper::mapToReadWorkspace)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkspaceReadDto> getNoneDeletedWorkspaces() {
        return repository.findAll().stream()
                .filter(w -> !w.isDeleted())
                .map(WorkspaceMapper::mapToReadWorkspace)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceReadDto getWorkspace(UUID id) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
    }

    @Override
    @Transactional
    public WorkspaceReadDto updateWorkspace(WorkspaceEditDto workspaceEditDto) {
        return WorkspaceMapper.mapToReadWorkspace(updateWorkspaceHelper(workspaceEditDto));
    }

    @Override
    public void deleteWorkspace(UUID id) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        workspaceEntity.setDeleted(true);
        repository.save(workspaceEntity);
    }

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {
        return null;
    }

    @Override
    public WorkspaceReadDto addUser(WorkspaceUserDto workspaceUserDto) {
        return WorkspaceMapper.mapToReadWorkspace(addUserHelper(workspaceUserDto));
    }

    @Override
    public WorkspaceReadDto removeUser(WorkspaceUserDto workspaceUserDto) {
        return WorkspaceMapper.mapToReadWorkspace(removeUserHelper(workspaceUserDto));
    }

    @Override
    public List<DocumentReadOnlyDto> getAllDocumentsByWorkspaceId(UUID workspaceId) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceId);
        return workspaceEntity.getDocuments().stream().map(DocumentMapper::mapToReadDocument).collect(Collectors.toList());
    }

    @Override
    public List<WorkspaceReadDto> getNoneDeletedWorkspaces(UUID userId) {
        return repository.findAll().stream()
                .filter(workspaceEntity -> workspaceEntity.isUserAuthorized(userId))
                .filter(workspaceEntity -> !workspaceEntity.isDeleted())
                .map(WorkspaceMapper::mapToReadWorkspace)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceReadDto addDocument(WorkspaceDocumentDto addDocument) {
        return WorkspaceMapper.mapToReadWorkspace(addDocumentHelper(addDocument));
    }

    @Override
    public WorkspaceReadDto removeDocument(WorkspaceDocumentDto removeDocument) {
        return WorkspaceMapper.mapToReadWorkspace(removeDocumentHelper(removeDocument));
    }

}
