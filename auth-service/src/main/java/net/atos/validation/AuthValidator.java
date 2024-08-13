package net.atos.validation;

import net.atos.dto.AuthDTO;
import org.springframework.stereotype.Component;

@Component
public class AuthValidator {

    public void validateAuth(AuthDTO authDTO) {
        if (authDTO == null) {
            throw new IllegalArgumentException("Nothing was entered");
        }
        if (authDTO.getEmail() == null || authDTO.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!isValidEmail(authDTO.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (authDTO.getPassword() == null || authDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
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

}
