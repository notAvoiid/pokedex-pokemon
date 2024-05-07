package com.crud.pokemon.repository;

import com.crud.pokemon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByUsername(String username);
    boolean existsByUsername(String username);
    @Query(value = "SELECT u FROM User u WHERE u.username = :username")
    Optional<User> loadByUsername(String username);
    Set<User> findAllByUsername(String username);

}
