package com.crud.pokemon.service.pokemon;

import com.crud.pokemon.controller.PokemonController;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Slf4j
public class PokemonServiceImpl implements PokemonService {

    private final AuthService authService;
    private final PokemonRepository repository;
    private final PagedResourcesAssembler<PokemonResponseDTO> assembler;

    public PokemonServiceImpl(AuthService authService, PokemonRepository repository, PagedResourcesAssembler<PokemonResponseDTO> assembler) {
        this.authService = authService;
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    @Transactional(readOnly = true)
    public PagedModel<EntityModel<PokemonResponseDTO>> findAll(Pageable pageable) {

        var pokemonPages = repository.findAll(pageable);
        var pokemonDTOPages = pokemonPages.map(PokemonResponseDTO::new);
        pokemonDTOPages.map(p -> p.add(linkTo(methodOn(PokemonController.class).findById(p.getId())).withSelfRel()));

        Link link = linkTo(methodOn(PokemonController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC"
        )).withSelfRel();


        log.info("Finding all Pokemon!");
        return assembler.toModel(pokemonDTOPages, link);
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
    public PagedModel<EntityModel<PokemonResponseDTO>> findByKeyword(String keyword, Pageable pageable) {

        var pokemonPages = repository.findByKeyword(keyword, pageable);
        var pokemonDTOPages = pokemonPages.map(PokemonResponseDTO::new);
        pokemonDTOPages.map(p -> p.add(linkTo(methodOn(PokemonController.class).findById(p.getId())).withSelfRel()));

        Link link = linkTo(methodOn(PokemonController.class).findAll(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                "ASC"
        )).withSelfRel();
        log.info("Finding a pokemon by his name/category/abilities");

        return assembler.toModel(pokemonDTOPages, link);
    }

    @Override
    @Transactional
    public PokemonResponseDTO save(PokemonRequestDTO request) {
            Pokemon pokemonToSave = new Pokemon(request);
        PokemonResponseDTO responseDTO = new PokemonResponseDTO(repository.save(pokemonToSave));
            log.info("Saved a Pokémon: {}", responseDTO.getName());
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
