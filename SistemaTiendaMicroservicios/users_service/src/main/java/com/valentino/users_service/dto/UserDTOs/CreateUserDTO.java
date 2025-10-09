package com.valentino.users_service.dto.UserDTOs;

import com.valentino.users_service.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record CreateUserDTO(@NotBlank String name,
                            @NotBlank String lastname,
                            @NotBlank String username,
                            @NotBlank String email,
                            @NotBlank String password,
                            @NotEmpty Set<Role> rolesList
                            ) {
}
