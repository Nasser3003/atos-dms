package net.atos.controller;

import lombok.RequiredArgsConstructor;
import net.atos.dto.AuthDto;
import net.atos.dto.LoginResponseDTO;
import net.atos.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {
    private final UserAuthService userAuthService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerEndpoint(@RequestBody AuthDto authDTO) {
        userAuthService.registerUser(authDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginEndpoint(@RequestBody AuthDto authDTO) {
        LoginResponseDTO response = userAuthService.loginUser(authDTO);
        return ResponseEntity.ok(response);
    }
}
