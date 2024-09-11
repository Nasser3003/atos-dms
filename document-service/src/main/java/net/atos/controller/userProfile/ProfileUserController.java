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

@RestController
@RequestMapping("/user/profile")
public class ProfileUserController extends AbstractProfileController {

    @Autowired
    public ProfileUserController(UserProfileUserService userProfileService) {
        super(userProfileService);
    }

    @Override
    @GetMapping("/documents/{userEmail}")
    ResponseEntity<List<DocumentReadOnlyDto>> getNoneDeletedDocuments(@PathVariable String userEmail) {
        return ResponseEntity.ok(profileService.getNoneDeletedDocuments(userEmail));
    }

    @Override
    @GetMapping("/workspaces/{userEmail}")
    ResponseEntity<List<WorkspaceReadDto>> getNoneDeletedWorkspaces(@PathVariable String userEmail) {
        return ResponseEntity.ok(profileService.getNoneDeletedWorkspaces(userEmail));
    }

}
