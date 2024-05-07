package com.crud.pokemon.model;

import com.crud.pokemon.model.dto.pokemon.PokemonRequestDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "pokemons")
@Getter
@Setter
public class Pokemon implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private String abilities;

    private String type;
    private String weakness;

    private String height;
    private String weight;

    @JsonIgnore
    @Column(nullable = false)
    private boolean active;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "wishlist",
            joinColumns = @JoinColumn(name = "pokemon_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;

    public Pokemon(){
        setActive(true);
    }

    public Pokemon(String name, String category, String abilities, String type, String weakness, String height, String weight) {
        this.name = name;
        this.category = category;
        this.abilities = abilities;
        this.type = type;
        this.weakness = weakness;
        this.height = height;
        this.weight = weight;
        setActive(true);
    }

    public void returner(PokemonRequestDTO request) {
        this.setName(request.getName());
        this.setCategory(request.getCategory());
        this.setAbilities(request.getAbilities());
        this.setType(request.getType());
        this.setWeakness(request.getWeakness());
        this.setHeight(request.getHeight());
        this.setWeight(request.getWeight());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon pokemon = (Pokemon) o;
        return Objects.equals(id, pokemon.id) && Objects.equals(name, pokemon.name) && Objects.equals(abilities, pokemon.abilities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, abilities);
    }
}
