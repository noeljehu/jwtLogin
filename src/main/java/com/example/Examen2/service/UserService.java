package com.example.Examen2.service;

import com.example.Examen2.model.User;  // Correct import for your custom User class
import com.example.Examen2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username); // Asegúrate de que este método esté bien implementado
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()) // Los roles de tu entidad User
                .build();
    }

    // Método para guardar un nuevo usuario
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Método para verificar si un usuario ya existe por su nombre de usuario
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }
}

