package com.crud.pokemon.service.pokemon;

import com.crud.pokemon.model.dto.pokemon.PokemonRequestDTO;
import com.crud.pokemon.model.dto.pokemon.PokemonResponseDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

public interface PokemonService {

    PagedModel<EntityModel<PokemonResponseDTO>> findAll(Pageable pageable);
    PokemonResponseDTO findById(Long id);
    PagedModel<EntityModel<PokemonResponseDTO>> findByKeyword(String keyword, Pageable pageable);
    PokemonResponseDTO save(PokemonRequestDTO pokemon);
    PokemonResponseDTO update(Long id, PokemonRequestDTO pokemon);
    void delete(Long id);
    void favorite(Long id);
    void unFavorite(Long id);
}
