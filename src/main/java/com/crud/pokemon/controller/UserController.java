package com.crud.pokemon.controller;

import com.crud.pokemon.model.dto.users.UpdateDTO;
import com.crud.pokemon.model.dto.users.UserDTO;
import com.crud.pokemon.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "Users", description = "Endpoint for managing Users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Find all Users!", description = "Find all Users!",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<List<UserDTO>> getAllUsers(HttpServletRequest request) {
        var registrationDTOList = userService.getAllUsers();
        return ResponseEntity.ok(registrationDTOList);
    }

    @GetMapping("/{username}")
    @Operation(operationId = "username", summary = "Find an User!", description = "Find an User by passing his Username in the URL!",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content (schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<UserDTO> findByUsername(@PathVariable String username, HttpServletRequest request) {
        UserDTO user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    @Operation(summary = "Updates an User!",
            description = "Updates an User by passing in a JSON representation of the User and his ID in the body!",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = @Content (schema = @Schema(implementation = UpdateDTO.class))),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<UpdateDTO> updateUser(@RequestBody UpdateDTO data, HttpServletRequest request) {
        userService.updateUser(data);
        return ResponseEntity.ok(new UpdateDTO(data.name(), data.username(), data.password()));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Deletes an User!",
            description = "Deletes an User by passing his ID in the URL!",
            tags = {"Users"},
            responses = {
                    @ApiResponse(description = "No content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            })
    public ResponseEntity<Object> deleteUser(@PathVariable String username, HttpServletRequest request) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
