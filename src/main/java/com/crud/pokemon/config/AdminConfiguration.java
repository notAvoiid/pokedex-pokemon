package com.crud.pokemon.config;

import com.crud.pokemon.model.User;
import com.crud.pokemon.model.enums.Role;
import com.crud.pokemon.repository.UserRepository;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class AdminConfiguration implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    Logger log = Logger.getLogger(AdminConfiguration.class.getName());

    public AdminConfiguration(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Dotenv dotenv = Dotenv
                .configure()
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
        var user = new User(
                "Administrator",
                dotenv.get("MY_ADMIN_USERNAME"),
                encoder.encode(dotenv.get("MY_ADMIN_PASSWORD")),
                Role.ADMIN);
        Optional<User> optionalUser = userRepository.loadByUsername(user.getUsername());
        optionalUser.ifPresentOrElse(
                userValue -> log.log(Level.INFO, String.format(
                                "-> Admin logged in: User(Name: %s, Username: %s, Role: %s)%n",
                                userValue.getName(), userValue.getUsername(), userValue.getRole())),
                () -> userRepository.save(user));
    }
}
