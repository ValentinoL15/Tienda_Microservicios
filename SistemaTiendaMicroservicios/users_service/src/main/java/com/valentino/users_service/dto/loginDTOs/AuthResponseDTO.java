package com.valentino.users_service.dto.loginDTOs;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.valentino.users_service.model.Role;

import java.util.Set;

@JsonPropertyOrder({"username", "message", "jwt", "status"})
public record AuthResponseDTO(String username,
                              String message,
                              String jwt,
                              Set<Role> roles,
                              boolean status) {
}