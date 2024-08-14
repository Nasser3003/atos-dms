package net.atos.service;

import lombok.RequiredArgsConstructor;
import net.atos.dto.AuthDto;
import net.atos.dto.LoginResponseDTO;
import net.atos.model.UserEntity;
import net.atos.repository.UserRepository;
import net.atos.validation.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAuthService {
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final AuthValidator authValidator;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticateManager;

    public void registerUser(AuthDto authDTO) {
        authValidator.validateRegistration(authDTO);
        userRepository.save(new UserEntity(authDTO.getEmail(),
                passwordEncoder.encode(authDTO.getPassword()), authDTO.getNationalId()));
    }

    public LoginResponseDTO loginUser(AuthDto authDto) {
        authValidator.validateAuth(authDto);

        Authentication auth = authenticateManager
                .authenticate(new UsernamePasswordAuthenticationToken(authDto
                        .getEmail(),authDto.getPassword())
        );

        String token = jwtTokenService.generateJWT(auth);
        UserEntity user = userRepository.findByEmail(authDto.getEmail()).get();

        return new LoginResponseDTO(user.getEmail(), token);
    }


    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
