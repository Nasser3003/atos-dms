package net.atos.service.workspace;

import net.atos.configuration.CustomJwtAuthenticationConverter;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.dto.workspace.WorkspaceDocumentDto;
import net.atos.dto.workspace.WorkspaceEditDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.dto.workspace.WorkspaceUserDto;
import net.atos.exception.UnauthorizedException;
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
public class WorkspaceUserService extends AbstractWorkspaceService {

    @Autowired
    public WorkspaceUserService(WorkspaceRepository repository, DocumentRepository documentRepository) {
        super(repository, documentRepository);
    }

    @Override
    public List<WorkspaceReadDto> getAllWorkspacesForUser() {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        return getAllWorkspacesForUser(authenticatedUserId);
    }

    @Override
    public List<WorkspaceReadDto> getAllWorkspaces() {
        throw new UnsupportedOperationException("This operation is not supported for this user type");
    }

    @Override
    public List<WorkspaceReadDto> getAllWorkspacesForUser(UUID userId) {
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
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();

        if (!workspaceEntity.isUserAuthorized(authenticatedUserId))
            throw new UnauthorizedException("you dont have permissions to access this workspace");
        return WorkspaceMapper.mapToReadWorkspace(workspaceEntity);
    }

    @Override
    @Transactional
    public WorkspaceReadDto updateWorkspace(WorkspaceEditDto workspaceEditDto) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceEditDto.getId());
        if (notWorkspaceOwner(workspaceEntity))
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

    @Override
    public WorkspaceReadDto addUser(WorkspaceUserDto workspaceUserDto) {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        if (!workspaceUserDto.getUserId().equals(authenticatedUserId))
            throw new UnauthorizedException("you dont have permissions to add this User");

        if (!findNoneDeletedWorkspace(workspaceUserDto.getWorkspaceId()).getCreatedByUserId().equals(authenticatedUserId))
            throw new UnauthorizedException("you dont have permissions to add edit this Workspace");
        return WorkspaceMapper.mapToReadWorkspace(addUserHelper(workspaceUserDto));
    }

    @Override
    public WorkspaceReadDto removeUser(WorkspaceUserDto workspaceUserDto) {
        UUID authenticatedUserId = CustomJwtAuthenticationConverter.extractUserIdFromContext();
        if (!workspaceUserDto.getUserId().equals(authenticatedUserId))
            throw new UnauthorizedException("you dont have permissions to add this User");
        if (!findNoneDeletedWorkspace(workspaceUserDto.getUserId()).getCreatedByUserId().equals(authenticatedUserId))
            throw new UnauthorizedException("you dont have permissions to add edit this Workspace");
        return WorkspaceMapper.mapToReadWorkspace(removeUserHelper(workspaceUserDto));
    }

    @Override
    public List<DocumentReadOnlyDto> getAllDocumentsByWorkspaceId(UUID workspaceId) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(workspaceId);
        if (notWorkspaceMember(workspaceEntity))
            throw new UnauthorizedException("you arent a member of this workspace");
        return workspaceEntity.getDocuments().stream().map(DocumentMapper::mapToReadDocument).collect(Collectors.toList());
    }

    @Override
    public List<WorkspaceReadDto> getNoneDeletedWorkspaces(UUID userId) {
        if (!CustomJwtAuthenticationConverter.extractUserIdFromContext().equals(userId))
            throw new UnauthorizedException("you dont have permissions to access this workspace");
        return repository.findAll().stream()
                .filter(workspaceEntity -> workspaceEntity.isUserAuthorized(userId))
                .filter(workspaceEntity -> !workspaceEntity.isDeleted())
                .map(WorkspaceMapper::mapToReadWorkspace)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceReadDto addDocument(WorkspaceDocumentDto addDocumentWorkspace) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(addDocumentWorkspace.getWorkspaceId());
        if (notWorkspaceOwner(workspaceEntity))
            throw new UnauthorizedException("You are not authorized to add documents to this workspace");
        if (documentRepository.findById(addDocumentWorkspace.getDocumentId()).isPresent())
            if (!documentRepository.findById(addDocumentWorkspace.getDocumentId()).get().getCreatedByUserId().equals(CustomJwtAuthenticationConverter.extractUserIdFromContext()))
                throw new UnauthorizedException("You are not authorized to add documents to this workspace");
        return WorkspaceMapper.mapToReadWorkspace(addDocumentHelper(addDocumentWorkspace));
    }

    @Override
    public WorkspaceReadDto removeDocument(WorkspaceDocumentDto removeDocumentWorkspace) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(removeDocumentWorkspace.getWorkspaceId());
        if (notWorkspaceOwner(workspaceEntity))
            throw new UnauthorizedException("You are not authorized to add documents to this workspace");
        if (documentRepository.findById(removeDocumentWorkspace.getDocumentId()).isPresent())
            if (!documentRepository.findById(removeDocumentWorkspace.getDocumentId()).get().getCreatedByUserId().equals(CustomJwtAuthenticationConverter.extractUserIdFromContext()))
                throw new UnauthorizedException("You are not authorized to add documents to this workspace");
        return WorkspaceMapper.mapToReadWorkspace(removeDocumentHelper(removeDocumentWorkspace));
    }
}
