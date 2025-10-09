package com.valentino.users_service.dto.loginDTOs;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank String username,
                       @NotBlank String password) {
}
