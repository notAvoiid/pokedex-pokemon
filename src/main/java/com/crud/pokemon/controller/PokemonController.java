package com.crud.pokemon.controller;

import com.crud.pokemon.model.dto.pokemon.PokemonRequestDTO;
import com.crud.pokemon.model.dto.pokemon.PokemonResponseDTO;
import com.crud.pokemon.service.pokemon.PokemonServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon")
@Tag(name = "Pokemons", description = "Endpoint for managing Pokemons")
public class PokemonController {

    private final PokemonServiceImpl service;

    public PokemonController(PokemonServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Find all Pokemons!", description = "Find all Pokemons!",
            tags = {"Pokemons"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = PokemonResponseDTO.class)))
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<List<PokemonResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findALl());
    }

    @GetMapping("/{id}")
    @Operation(operationId = "id", summary = "Find a Pokemon!", description = "Find a Pokemon by passing its ID!",
            tags = {"Pokemons"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content (schema = @Schema(implementation = PokemonResponseDTO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<PokemonResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/findByAll")
    @Operation(summary = "Finds all Pokemons by passing keywords!", description = "Finds all Pokemons by passing keywords!",
            tags = {"Pokemons"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = PokemonResponseDTO.class)))
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<List<PokemonResponseDTO>> findByKeyword(
            @RequestParam(required = false) String keyword
            ) {
        return ResponseEntity.ok(service.findByKeyword(keyword));
    }

    @PostMapping("/save")
    @Operation(summary = "Adds a new Pokemon!",
            description = "Adds a new Pokemon by passing in a JSON representation of the pokemon!",
            tags = {"Pokemons"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content (schema = @Schema(implementation = PokemonResponseDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<PokemonResponseDTO> save(@RequestBody @Valid PokemonRequestDTO pokemon) {
        return ResponseEntity.ok(service.save(pokemon));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates a Pokemon!",
            description = "Updates a Pokemon by passing in a JSON representation of the pokemon and its ID in the url!",
            tags = {"Pokemons"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content (schema = @Schema(implementation = PokemonResponseDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<PokemonResponseDTO> update(@PathVariable Long id, @RequestBody @Valid PokemonRequestDTO pokemon) {
        return ResponseEntity.ok(service.update(id, pokemon));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletes a Pokemon!",
            description = "Deletes a Pokemon by passing its ID!",
            tags = {"Pokemons"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/favorite/{id}")
    @Operation(summary = "Favorites a Pokemon!",
            description = "Favorites a Pokemon by passing its ID and the User token!",
            tags = {"Pokemons"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content (schema = @Schema(implementation = PokemonResponseDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<String> favorite(@PathVariable Long id) {
        service.favorite(id);
        return ResponseEntity.ok("Favorited");
    }

    @PostMapping("/unfavorite/{id}")
    @Operation(summary = "Unfavorites a Pokemon!",
            description = "Unfavorites a Pokemon by passing its ID and the User token!",
            tags = {"Pokemons"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content (schema = @Schema(implementation = PokemonResponseDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<String> unFavorite(@PathVariable Long id) {
        service.unFavorite(id);
        return ResponseEntity.ok("Unfavorited");
    }
}
