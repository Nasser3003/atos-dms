package net.atos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.atos.model.UserEntity;
import net.atos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class Runner implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("abdo.abdo3003@gmail.com").isPresent())
            return;

        UserEntity user1 = new UserEntity("abdo.abdo3003@gmail.com", encoder.encode("123"));
        userRepository.save(user1);
    }
}