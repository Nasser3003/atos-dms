package net.atos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.atos.model.EnumRole;
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
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("abdo.abdo3003@gmail.com").isPresent())
            return;

        UserEntity user = new UserEntity("abdo.abdo3003@gmail.com",
                passwordEncoder.encode("12345678"), "123");
        user.addRole(EnumRole.ADMIN);
        userRepository.save(user);
    }
}