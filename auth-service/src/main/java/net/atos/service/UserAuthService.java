package net.atos.service;

import lombok.AllArgsConstructor;
import net.atos.configuration.SecurityConfig;
import net.atos.dto.AuthDTO;
import net.atos.model.User;
import net.atos.repository.UserInformationRepository;
import net.atos.validation.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserAuthService {
    private UserInformationRepository userRepository;
    private SecurityConfig securityConfig;
    private AuthValidator authValidator;

    public void registerUser(AuthDTO authDTO) {
        authValidator.validateAuth(authDTO);
        userRepository.save(new User(authDTO.getEmail(),
                securityConfig.passwordEncoder().encode(authDTO.getPassword())));
    }

    public String login(AuthDTO authDTO) {
        authValidator.validateAuth(authDTO);
        // TODO
        return null;
    }
}
