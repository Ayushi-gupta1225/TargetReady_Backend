package com.target.planogram.service;

import com.target.planogram.entity.User;
import com.target.planogram.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Create root user if not exists
        Optional<User> existingRootUser = userRepository.findByUserName("root");
        if (existingRootUser.isEmpty()) {
            User rootUser = new User();
            rootUser.setUserId(UUID.randomUUID().toString());
            rootUser.setUserName("root");
            rootUser.setPassword(passwordEncoder.encode("admin"));
            rootUser.setRole("ROLE_ADMIN");
            userRepository.save(rootUser);
            System.out.println("Root user created with ROLE_ADMIN");
        }
    }

    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findByUserName(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with username " + user.getUsername() + " already exists");
        }
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER"); // Default role for other users
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
