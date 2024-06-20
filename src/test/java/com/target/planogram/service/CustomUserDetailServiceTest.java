package com.target.planogram.service;

import static org.junit.jupiter.api.Assertions.*;

import com.target.planogram.entity.User;
import com.target.planogram.repository.UserRepository;
import com.target.planogram.service.CustomUserDetailService;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Log
public class CustomUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailService customUserDetailService;


    @Test
    public void testLoadUserByUsername() {
        // Mock the behavior of the userRepository
        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(new User("testId","testUser", "password")));

        // Call the method to test
        UserDetails userDetails = customUserDetailService.loadUserByUsername("testUser");
        log.info(userDetails.toString());
        // Assert the result
        assertEquals("testUser", userDetails.getUsername());
    }
}