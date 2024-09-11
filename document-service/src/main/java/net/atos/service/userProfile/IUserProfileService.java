package net.atos.service.userProfile;

import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.dto.workspace.WorkspaceReadDto;

import java.util.List;

public interface IUserProfileService {

    List<DocumentReadOnlyDto> getNoneDeletedDocuments(String userEmail);
    List<WorkspaceReadDto> getNoneDeletedWorkspaces(String userEmail);

}