package com.crud.pokemon.service.user;

import com.crud.pokemon.exceptions.NullUserException;
import com.crud.pokemon.exceptions.UserRegisteredException;
import com.crud.pokemon.exceptions.WrongMatchPasswordUsernameException;
import com.crud.pokemon.model.User;
import com.crud.pokemon.model.dto.users.*;
import com.crud.pokemon.repository.UserRepository;
import com.crud.pokemon.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final ApplicationContext context;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder encoder;

    public UserService(ApplicationContext context, UserRepository userRepository, AuthService authService, PasswordEncoder encoder) {
        this.context = context;
        this.userRepository = userRepository;
        this.authService = authService;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public LoginResponseDTO login(@RequestBody AuthenticationDTO data) {

        UserDetails user = userRepository.findByUsername(data.username());
        if (user == null || !encoder.matches(data.password(), user.getPassword())) throw new WrongMatchPasswordUsernameException();

        AuthenticationManager authenticationManager = context.getBean(AuthenticationManager.class);
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = authService.generateToken((User) auth.getPrincipal());

        log.info("An user is authenticated!");

        return new LoginResponseDTO(auth.getName(), token);
    }

    @Transactional
    public LoginResponseDTO register(@RequestBody RegisterDTO data) {

        if (this.userRepository.existsByUsername(data.username())) throw new UserRegisteredException();
        User newUser = new User(data.name(), data.username(), encoder.encode(data.password()), data.role());
        this.userRepository.save(newUser);
        var token = authService.generateToken(newUser);

        log.info("Registered an user!!");

        return new LoginResponseDTO(data.username(), token);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();

        log.info("Finding all users!");

        return userList.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getRole(),
                        user.getWishlist()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO findByUsername(String username) {
        User user = (User) userRepository.findByUsername(username);

        log.info("Finding an user!");

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getWishlist());
    }

    @Transactional
    public void updateUser(UpdateDTO data) {
        Optional<User> optionalUser = authService.getAuthUser();
        optionalUser.ifPresent(user -> {
            var usedDataList = userRepository.findAllByUsername(data.username());
            if (usedDataList.stream().anyMatch(userValue -> !userValue.getId().equals(user.getId())))
                throw new UserRegisteredException();
            user.setName(data.name());
            user.setUsername(data.username());
            user.setPassword(encoder.encode(data.password()));

            log.info("Updating an user!");
            userRepository.save(user);
        });
    }

    @Transactional
    public void deleteUser(String username) {
        User user = (User) userRepository.findByUsername(username);
        if(user != null) {
            user.getWishlist().forEach(product -> product.getUsers().remove(user));
            userRepository.delete(user);
            log.info("Deleting an user!");
        } else
            throw new NullUserException();
    }
}
