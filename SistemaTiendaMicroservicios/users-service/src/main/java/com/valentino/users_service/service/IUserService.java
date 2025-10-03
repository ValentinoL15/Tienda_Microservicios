package com.valentino.users_service.service;

import com.valentino.users_service.dto.UserDTOs.CreateUserDTO;
import com.valentino.users_service.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    List<User> getAllUsers();

    Optional<User> getUserById(Long id);

    public User getUserByCarrito(Long carrito_id);

    CreateUserDTO createUser(CreateUserDTO usersDTO);

    public void addCarrito(Long user_id, Long carritoId);

    void deleteUserById(Long id);

    User updateUser(Long id, User user);

    public String encryptPassword(String password);

}
