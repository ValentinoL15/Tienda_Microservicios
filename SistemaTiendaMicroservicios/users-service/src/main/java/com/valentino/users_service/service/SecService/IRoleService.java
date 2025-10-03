package com.valentino.users_service.service.SecService;

import com.valentino.users_service.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    List<Role> findAllRoles();

    Optional<Role> findRoleById(Long id);

    Role saveRole(Role role);

    void deleteRoleById(Long id);

    //Role updateRole(Long id, UpdateRoleDTO role);

}
