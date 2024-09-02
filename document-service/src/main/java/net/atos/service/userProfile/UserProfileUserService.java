package net.atos.service.userProfile;

import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.service.document.DocumentUserService;
import net.atos.service.workspace.WorkspaceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserProfileUserService extends AbstractUserProfileService {

    @Autowired
    public UserProfileUserService(DocumentUserService documentUserService, WorkspaceUserService workspaceUserService) {
        super(documentUserService, workspaceUserService);
    }


    public List<DocumentReadOnlyDto> getNoneDeletedDocuments(UUID userId) {
        List<DocumentReadOnlyDto> documents = documentService.getAllNoneDeletedDocumentsForUser(userId);
        return documents.stream().filter(doc -> doc.getAccessibleByUsers().contains(userId)).collect(Collectors.toList());
    }

    public List<WorkspaceReadDto> getNoneDeletedWorkspaces(UUID userId) {
        List<WorkspaceReadDto> documents = workspaceService.getNoneDeletedWorkspaces(userId);
        return documents.stream().filter(work -> work.getAccessibleByUsers().contains(userId)).collect(Collectors.toList());
    }
}