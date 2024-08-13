package net.atos.controller;

import lombok.AllArgsConstructor;
import net.atos.dto.AuthDTO;
import net.atos.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/auth")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {
    private UserAuthService userAuthService;

    @PostMapping("/register")
    public void register(AuthDTO authDTO) {
        userAuthService.registerUser(authDTO);
    }

    @PostMapping("/login")
    public String login (AuthDTO authDTO) {
        return userAuthService.login(authDTO);
    }
}
