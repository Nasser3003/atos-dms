package net.atos.service.userProfile;

import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.dto.workspace.WorkspaceReadDto;

import java.util.List;
import java.util.UUID;

public interface IUserProfileService {

    List<DocumentReadOnlyDto> getNoneDeletedDocuments(UUID userId);
    List<WorkspaceReadDto> getNoneDeletedWorkspaces(UUID userId);

}