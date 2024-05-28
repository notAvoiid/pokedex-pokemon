package com.crud.pokemon.repository;

import com.crud.pokemon.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PokemonRepository extends JpaRepository<Pokemon, Long> {

    @Query("SELECT p FROM Pokemon p WHERE LOWER(p.name) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(p.category) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(p.abilities) LIKE LOWER(concat('%', :keyword, '%'))")
    Page<Pokemon> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
