package com.valentino.users_service.service;

import com.valentino.users_service.dto.CarritoDTO;
import com.valentino.users_service.dto.UserDTOs.CreateUserDTO;
import com.valentino.users_service.exceptions.BadRequestException;
import com.valentino.users_service.model.Role;
import com.valentino.users_service.model.User;
import com.valentino.users_service.repository.IApiCarritos;
import com.valentino.users_service.repository.IRoleRepository;
import com.valentino.users_service.repository.IUserRepository;
import com.valentino.users_service.service.SecService.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService{

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IApiCarritos apiCarritos;

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUserByCarrito(Long id_carrito) {
        List<User> myUsers = userRepository.findAll();
        CarritoDTO carrito = apiCarritos.getUserByCarrito(id_carrito);
        System.out.println(carrito);
        User myUser = null;
        for (User user : myUsers) {
            for (Long carri : user.getCarrito_id()) {
                if (carri.equals(id_carrito)) {
                    myUser = user;
                    System.out.println(myUser.getUsername());
                }
            }
        }
        return myUser;
    }

    @Override
    public CreateUserDTO createUser(CreateUserDTO usersDTO) {

        List<User> usersList = userRepository.findAll();

        Set<Role> validateRole = new HashSet<>();
        for(Role role: usersDTO.rolesList()) {
            Role rol = roleRepository.findById(role.getId())
                    .orElseThrow(() -> new RuntimeException("No se encuentra el rol"));
            validateRole.add(rol);
        }

        if(usersDTO.name() == null || usersDTO.name().trim().length() < 3) {
            throw new BadRequestException("El nombre debe contener como mínimo 3 letras");
        }
        if(usersDTO.lastname() == null || usersDTO.lastname().trim().length() < 3) {
            throw new BadRequestException("El apellido debe contener como mínimo 3 letras");
        }

        if(usersDTO.email() == null || !usersDTO.email().matches(EMAIL_REGEX)){
            throw new IllegalStateException("El email ingresado no es válido");
        }

        if(usersDTO.username() == null){
            throw new BadRequestException("Debes ingresar un username");
        }
        for(User user: usersList) {
            if(usersDTO.username().equals(user.getUsername())) {
                throw new BadRequestException("EL username ya existe, por favor intente con otro");
            }
        }
        if(usersDTO.password() == null) {
            throw new BadRequestException("Por favor establezca una contraseña válida");
        }

        User user = null;

        try {
            user = new User();

            user.setName(usersDTO.name());
            user.setLastname(usersDTO.lastname());
            user.setUsername(usersDTO.username());
            user.setEmail(usersDTO.email());
            user.setPassword(encryptPassword(usersDTO.password()));
            user.setRolesList(validateRole);

            userRepository.save(user);

            apiCarritos.createDefectCarrito(user.getUser_id());

            return usersDTO;
        } catch (Exception e) {
            if (user != null && user.getUser_id() != null) {
                userRepository.delete(user); // rollback manual
            }
            throw new RuntimeException("Error al creae usuario y carrito", e);
        }
    }

    @Override
    public void addCarrito(Long user_id, Long carritoId) {
    User myUser = userRepository.findById(user_id)
            .orElseThrow(() -> new RuntimeException("Error al buscar user"));

    myUser.getCarrito_id().add(carritoId);
    userRepository.save(myUser);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No se encuentra el usuario"));
        if(user.getUsername() != null) user.setUsername(user.getUsername());
        if(user.getRolesList() != null){
            Set<Role> rolesList = new HashSet<>();
            for(Role rol : user.getRolesList()) {
                Role attached = roleRepository.findById(rol.getId())
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
                rolesList.add(attached);
            }
            existingUser.setRolesList(rolesList);
        }
        return userRepository.save(existingUser);
    }

    @Override
    public String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }
}
