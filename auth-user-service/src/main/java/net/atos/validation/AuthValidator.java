package net.atos.validation;

import lombok.RequiredArgsConstructor;
import net.atos.dto.AuthDto;
import net.atos.repository.UserRepository;
import net.atos.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthValidator {

    private final UserRepository repository;

    public void validateAuth(AuthDto authDTO) {
        if (authDTO == null)
            throw new NullAuthDtoException();

        if (authDTO.getEmail() == null || authDTO.getEmail().trim().isEmpty())
            throw new EmptyFieldException("Email");

        if (!isValidEmail(authDTO.getEmail()))
            throw new InvalidEmailFormatException();

        if (authDTO.getPassword() == null || authDTO.getPassword().isEmpty())
            throw new EmptyFieldException("Password");
    }

    public void validateRegistration(AuthDto authDTO) {
        validateAuth(authDTO);
        if (isEmailTaken(authDTO.getEmail()))
            throw new EmailAlreadyTakenException();

        if (authDTO.getPassword().length() < 8)
            throw new PasswordTooShortException();
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