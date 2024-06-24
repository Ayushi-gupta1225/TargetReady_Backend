//package com.target.planogram.service;
//import com.target.planogram.entity.User;
//import com.target.planogram.repository.UserRepository;
//import lombok.extern.java.Log;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ActiveProfiles("test")
//@Log
//public class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private UserService userService;
//
//    @Test
//    public void getUsers_returnsUsersFromRepository() {
//        List<User> users = Collections.singletonList(new User());
//        when(userRepository.findAll()).thenReturn(users);
//
//        List<User> result = userService.getUsers();
//
//        assertEquals(users, result);
//        verify(userRepository, times(1)).findAll();
//    }
//
//    @Test
//    public void createUser_encodesPasswordAndSavesUser() {
//        User user = new User();
//        String encodedPassword = "encodedPassword";
//        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
//        log.info(user.toString());
//        User savedUser = userService.createUser(user);
//
//        verify(userRepository, times(1)).save(user);
//    }
//}