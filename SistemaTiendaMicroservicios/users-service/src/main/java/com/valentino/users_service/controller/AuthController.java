package com.valentino.users_service.controller;

import com.valentino.users_service.dto.UserDTOs.CreateUserDTO;
import com.valentino.users_service.dto.loginDTOs.AuthResponseDTO;
import com.valentino.users_service.dto.loginDTOs.LoginDTO;
import com.valentino.users_service.exceptions.BadRequestException;
import com.valentino.users_service.model.User;
import com.valentino.users_service.repository.IUserRepository;
import com.valentino.users_service.service.IUserService;
import com.valentino.users_service.service.UserDetailsServiceImp;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getUser(@PathVariable Long user_id){
        try {
            User user = userService.getUserById(user_id)
                    .orElseThrow(() -> new RuntimeException("No se encuentra el usuario"));
            return ResponseEntity.ok(user);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/getUserByName/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username){
        try {
            User user = userRepository.findUserEntityByUsername(username)
                    .orElseThrow(() -> new RuntimeException("No se encuentra el user"));
            return ResponseEntity.ok(user);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody CreateUserDTO userDTO) {
        try {
            CreateUserDTO usuario = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Ocurrió un error inesperado"));
        }
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/{user_id}/carrito/{carritoId}")
    public ResponseEntity<Void> addCarritoToUser(
            @PathVariable Long user_id,
            @PathVariable Long carritoId) {
        userService.addCarrito(user_id,carritoId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/userByCarrito/{id_carrito}")
    public ResponseEntity<?> getUserByCarrito(@PathVariable Long id_carrito) {
            User myUser = userService.getUserByCarrito(id_carrito);
            return ResponseEntity.ok(myUser);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO login) {
        try {
            AuthResponseDTO response = userDetailsService.loginUser(login);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Usuario no encontrado"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Contraseña incorrecta"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error interno"));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/private")
    public ResponseEntity<?> getRoute(){
        try {
            return ResponseEntity.ok("Podes pasar...");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No estás autorizado/a"));
        }
    }



}
