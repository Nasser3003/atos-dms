package net.atos.validation;

import lombok.RequiredArgsConstructor;
import net.atos.dto.AuthDto;
import net.atos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthValidator {

    private final UserRepository repository;

    public void validateAuth(AuthDto authDTO) {
        if (authDTO == null) {
            throw new IllegalArgumentException("Nothing was entered");
        }
        if (authDTO.getEmail() == null || authDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!isValidEmail(authDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (authDTO.getPassword() == null || authDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

    public void validateRegistration(AuthDto authDTO) {
        validateAuth(authDTO);
        if (isEmailTaken(authDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }
        if (authDTO.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    private boolean isValidEmail(String email) {
        final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return email.matches(EMAIL_REGEX);
    }

    private boolean isEmailTaken(String email) {
        return repository.findByEmail(email).isPresent();
    }

}
