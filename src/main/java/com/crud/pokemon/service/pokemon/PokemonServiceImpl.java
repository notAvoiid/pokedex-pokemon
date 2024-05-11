package com.crud.pokemon.service.pokemon;

import com.crud.pokemon.exceptions.EntityNotFoundException;
import com.crud.pokemon.exceptions.NullPokemonException;
import com.crud.pokemon.exceptions.WishListPokemonException;
import com.crud.pokemon.model.Pokemon;
import com.crud.pokemon.model.User;
import com.crud.pokemon.model.dto.pokemon.PokemonRequestDTO;
import com.crud.pokemon.model.dto.pokemon.PokemonResponseDTO;
import com.crud.pokemon.repository.PokemonRepository;
import com.crud.pokemon.service.auth.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PokemonServiceImpl implements PokemonService {

    private final AuthService authService;
    private final PokemonRepository repository;

    public PokemonServiceImpl(AuthService authService, PokemonRepository repository) {
        this.authService = authService;
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PokemonResponseDTO> findALl() {

        List<Pokemon> data = repository.findAll();

        log.info("Finding all Pokemon!");
        return data.stream().map(PokemonResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PokemonResponseDTO findById(Long id) {

        Pokemon data = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("id:%s not found", id)));

        log.info("Finding a pokemon by his id!");

        return new PokemonResponseDTO(data);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PokemonResponseDTO> findByKeyword(String keyword) {

        List<Pokemon> data = repository.findByKeyword(keyword);
        log.info("Finding a pokemon by his name/category/abilities");

        return data.stream().map(PokemonResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PokemonResponseDTO save(PokemonRequestDTO request) {
            Pokemon pokemonToSave = new Pokemon(request);
            PokemonResponseDTO responseDTO = new PokemonResponseDTO(repository.save(pokemonToSave));
            log.info("Saved a Pokémon: {}", responseDTO.name());
            return responseDTO;
    }

    @Override
    @Transactional
    public PokemonResponseDTO update(Long id, PokemonRequestDTO pokemon) {
        Pokemon data = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("id:%s not found", id)));
        data.returner(pokemon);
        PokemonResponseDTO response = new PokemonResponseDTO(repository.save(data));
        log.info("Updating a pokemon!");

        return response;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
        log.info("Deleting a pokemon!");
    }

    @Override
    public void favorite(Long id) {
        Optional<User> optionalUser = authService.getAuthUser();
        optionalUser.ifPresent(
                user -> {
                    Optional<Pokemon> optionalPokemon = repository.findById(id);
                    if (optionalPokemon.isPresent() && optionalPokemon.get().isActive()) {
                        var pokemon = optionalPokemon.get();
                        var wishList = user.getWishlist();
                        var userList = pokemon.getUsers();
                        if (!wishList.contains(pokemon)) {
                            wishList.add(pokemon);
                            userList.add(user);
                            repository.save(pokemon);
                        } else {
                            throw new WishListPokemonException();
                        }
                    } else {
                        throw new NullPokemonException();
                    }
                }
        );
    }

    @Override
    public void unFavorite(Long id) {
        Optional<User> optionalUser = authService.getAuthUser();
        optionalUser.ifPresent(
                user -> {
                    Optional<Pokemon> optionalPokemon = repository.findById(id);
                    if (optionalPokemon.isPresent() && optionalPokemon.get().isActive()) {
                        var pokemon = optionalPokemon.get();
                        var wishlist = user.getWishlist();
                        var userList = pokemon.getUsers();
                        if (wishlist.contains(pokemon)) {
                            wishlist.remove(pokemon);
                            userList.remove(user);
                            repository.save(pokemon);
                        } else {
                            throw new WishListPokemonException();
                        }
                    } else {
                        throw new NullPokemonException();
                    }
                }
        );
    }
}
