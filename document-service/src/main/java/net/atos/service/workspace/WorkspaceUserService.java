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
        String userEmail = CustomJwtAuthenticationConverter.extractUserEmailFromContext();
        return getAllWorkspacesForUser(userEmail);
    }

    @Override
    public List<WorkspaceReadDto> getAllWorkspaces() {
        throw new UnsupportedOperationException("This operation is not supported for this user type");
    }

    @Override
    public List<WorkspaceReadDto> getAllWorkspacesForUser(String userEmail) {
        return repository.findAll().stream()
                .filter(workspaceEntity -> workspaceEntity.isUserAuthorized(userEmail))
                .filter(w -> !w.isDeleted())
                .map(WorkspaceMapper::mapToReadWorkspace)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceReadDto getWorkspace(UUID id) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        String userEmail = CustomJwtAuthenticationConverter.extractUserEmailFromContext();

        if (!workspaceEntity.isUserAuthorized(userEmail))
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
        String userEmail = CustomJwtAuthenticationConverter.extractUserEmailFromContext();

        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(id);
        if (!workspaceEntity.getCreatedByUser().equals(userEmail))
            throw new UnauthorizedException("you dont have permissions to delete this workspace");
        workspaceEntity.setDeleted(true);
        repository.save(workspaceEntity);
    }

    @Override
    public WorkspaceReadDto addUser(WorkspaceUserDto workspaceUserDto) {
        String userEmail = CustomJwtAuthenticationConverter.extractUserEmailFromContext();

        if (!findNoneDeletedWorkspace(workspaceUserDto.getWorkspaceId()).getCreatedByUser().equals(userEmail))
            throw new UnauthorizedException("you dont have permissions to add edit this Workspace");
        return WorkspaceMapper.mapToReadWorkspace(addUserHelper(workspaceUserDto));
    }

    @Override
    public WorkspaceReadDto removeUser(WorkspaceUserDto workspaceUserDto) {
        String currentUserEmail = CustomJwtAuthenticationConverter.extractUserEmailFromContext();
        String workspaceOwnerEmail = findNoneDeletedWorkspace(workspaceUserDto.getWorkspaceId()).getCreatedByUser();
        String userToRemoveEmail = workspaceUserDto.getUserEmail();

        if (!currentUserEmail.equals(userToRemoveEmail) && !currentUserEmail.equals(workspaceOwnerEmail))
            throw new UnauthorizedException("You don't have permissions to remove this user");

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
    public List<WorkspaceReadDto> getNoneDeletedWorkspaces(String email) {
        if (!CustomJwtAuthenticationConverter.extractUserEmailFromContext().equals(email))
            throw new UnauthorizedException("you dont have permissions to access this workspace");
        return repository.findAll().stream()
                .filter(workspaceEntity -> workspaceEntity.isUserAuthorized(email))
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
            if (!documentRepository.findById(addDocumentWorkspace.getDocumentId()).get().getCreatedByUser().equals(CustomJwtAuthenticationConverter.extractUserEmailFromContext()))
                throw new UnauthorizedException("You are not authorized to share this document");
        return WorkspaceMapper.mapToReadWorkspace(addDocumentHelper(addDocumentWorkspace));
    }

    @Override
    public WorkspaceReadDto removeDocument(WorkspaceDocumentDto removeDocumentWorkspace) {
        WorkspaceEntity workspaceEntity = findNoneDeletedWorkspace(removeDocumentWorkspace.getWorkspaceId());
        if (notWorkspaceOwner(workspaceEntity))
            throw new UnauthorizedException("You are not authorized to this workspace");
        return WorkspaceMapper.mapToReadWorkspace(removeDocumentHelper(removeDocumentWorkspace));
    }
}
