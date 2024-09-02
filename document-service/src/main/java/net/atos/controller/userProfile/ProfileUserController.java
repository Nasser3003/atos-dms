package net.atos.controller.userProfile;

import net.atos.dto.document.DocumentReadOnlyDto;
import net.atos.dto.workspace.WorkspaceReadDto;
import net.atos.service.userProfile.UserProfileUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/profile")
public class ProfileUserController extends AbstractProfileController {

    @Autowired
    public ProfileUserController(UserProfileUserService userProfileService) {
        super(userProfileService);
    }

    @Override
    @GetMapping("/documents/{userId}")
    ResponseEntity<List<DocumentReadOnlyDto>> getNoneDeletedDocuments(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getNoneDeletedDocuments(userId));
    }

    @Override
    @GetMapping("/workspaces/{userId}")
    ResponseEntity<List<WorkspaceReadDto>> getNoneDeletedWorkspaces(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getNoneDeletedWorkspaces(userId));
    }

}
