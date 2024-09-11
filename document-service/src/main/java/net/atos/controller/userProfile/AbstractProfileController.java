package net.atos.controller.userProfile;

import lombok.RequiredArgsConstructor;
import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.service.userProfile.IUserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractProfileController {

    final IUserProfileService profileService;

    abstract ResponseEntity<List<DocumentReadOnlyDto>> getNoneDeletedDocuments(@PathVariable String userEmail);

    abstract ResponseEntity<List<WorkspaceReadDto>> getNoneDeletedWorkspaces(@PathVariable String userEmail);

}