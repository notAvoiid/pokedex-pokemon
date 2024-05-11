package com.crud.pokemon.model.dto.pokemon;

import com.crud.pokemon.model.Pokemon;

public record PokemonResponseDTO (
    Long id,
    String name,
    String category,
    String abilities,
    String type,
    String weakness,
    String height,
    String weight
){
    public PokemonResponseDTO(Pokemon pokemon) {
        this(
                pokemon.getId(),
                pokemon.getName(),
                pokemon.getCategory(),
                pokemon.getAbilities(),
                pokemon.getType(),
                pokemon.getWeakness(),
                pokemon.getHeight(),
                pokemon.getWeight());
    }
}
