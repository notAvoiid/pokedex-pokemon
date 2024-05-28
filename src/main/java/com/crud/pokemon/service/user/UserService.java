package com.crud.pokemon.service.user;

import com.crud.pokemon.model.dto.users.*;

import java.util.List;

public interface UserService {

    LoginResponseDTO login(AuthenticationDTO data);
    LoginResponseDTO register(RegisterDTO data);
    List<UserDTO> getAllUsers();
    UserDTO findByUsername(String username);
    void updateUser(UpdateDTO data);
    void deleteUser(String username);

}
