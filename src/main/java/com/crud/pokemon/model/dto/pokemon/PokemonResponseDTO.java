package com.crud.pokemon.model.dto.pokemon;

import com.crud.pokemon.model.Pokemon;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class PokemonResponseDTO extends RepresentationModel<PokemonResponseDTO> {

    private Long id;
    private String name;
    private String category;
    private String abilities;
    private String type;
    private String weakness;
    private String height;
    private String weight;

    public PokemonResponseDTO(Pokemon pokemon) {
        this.id = pokemon.getId();
        this.name = pokemon.getName();
        this.category = pokemon.getCategory();
        this.abilities = pokemon.getAbilities();
        this.type = pokemon.getType();
        this.weakness = pokemon.getWeakness();
        this.height = pokemon.getHeight();
        this.weight = pokemon.getWeight();
    }
}
