package com.valentino.users_service.controller.SecController;

import com.valentino.users_service.model.Permission;
import com.valentino.users_service.model.Role;
import com.valentino.users_service.service.SecService.IPermissionService;
import com.valentino.users_service.service.SecService.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permissionService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getRoles")
    public ResponseEntity<List<Role>> getRoles() {
        try {
            List<Role> role = roleService.findAllRoles();
            return ResponseEntity.ok(role);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getRole/{id}")
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        Optional<Role> role = roleService.findRoleById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear-role")
    public ResponseEntity<Role> crearRole(@RequestBody Role role) {
        try {
            Set<Permission> permisions = new HashSet<>();
            Permission readPermision;
            for(Permission per : role.getPermissionList()) {
                readPermision = permissionService.findPermissionById(per.getId())
                        .orElseThrow(() -> new RuntimeException("No se encuntra ese permiso"));
                if (readPermision != null) {
                    permisions.add(readPermision);
                }
            }
            role.setPermissionList(permisions);
            Role newRole = roleService.saveRole(role);
            return ResponseEntity.ok(newRole);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
