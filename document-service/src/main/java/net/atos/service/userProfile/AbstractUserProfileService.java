package net.atos.service.userProfile;

import lombok.RequiredArgsConstructor;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.dto.workspace.WorkspaceSimpleDto;
import net.atos.mapper.DocumentMapper;
import net.atos.mapper.WorkspaceMapper;
import net.atos.model.DocumentEntity;
import net.atos.model.WorkspaceEntity;
import net.atos.service.document.IDocumentService;
import net.atos.service.workspace.IWorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractUserProfileService implements IUserProfileService {

    final IDocumentService documentService;
    final IWorkspaceService workspaceService;

    List<DocumentReadOnlyDto> getDocumentsHelper(List<DocumentEntity> documents) {
        return documents.stream()
                .map(DocumentMapper::mapToReadDocument)
                .collect(Collectors.toList());
    }

    List<WorkspaceSimpleDto> getWorkspacesHelper(List<WorkspaceEntity> workspaces) {
        return workspaces.stream().map(WorkspaceMapper::mapToSimpleWorkspaceDto).collect(Collectors.toList());
    }

}