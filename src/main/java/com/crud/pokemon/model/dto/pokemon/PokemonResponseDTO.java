package com.crud.pokemon.model.dto.pokemon;

public record PokemonResponseDTO (Long id,
    String name,
    String category,
    String abilities,
    String type,
    String weakness,
    String height,
    String weight
){}
