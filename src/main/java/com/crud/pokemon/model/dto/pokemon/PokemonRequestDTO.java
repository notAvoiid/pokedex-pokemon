package com.crud.pokemon.model.dto.pokemon;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PokemonRequestDTO {

    @NotBlank(message = "Name must not be blank!")
    @Size(min = 4, max = 30, message = "Name must be between 4 and 20 characters")
    private String name;

    @NotBlank(message = "Category must not be blank!")
    @Size(min = 4, max = 30, message = "Category must be between 4 and 20 characters")
    private String category;

    @NotBlank(message = "Abilities must not be blank!")
    @Size(min = 4, max = 30, message = "Abilities must be between 4 and 20 characters")
    private String abilities;

    @NotBlank(message = "Type must not be blank!")
    @Size(min = 4, max = 30, message = "Type must be between 4 and 20 characters")
    private String type;

    @NotBlank(message = "Weakness must not be blank!")
    @Size(min = 4, max = 30, message = "Weakness must be between 4 and 20 characters")
    private String weakness;

    @NotBlank(message = "Height must not be blank!")
    @Size(min = 4, max = 30, message = "Height must be between 4 and 20 characters")
    private String height;

    @NotBlank(message = "Weight must not be blank!")
    @Size(min = 4, max = 30, message = "Weight must be between 4 and 20 characters")
    private String weight;

}
