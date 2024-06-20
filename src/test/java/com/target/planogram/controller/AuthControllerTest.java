package com.target.planogram.controller;

import com.target.planogram.entity.User;
import com.target.planogram.models.JwtRequest;
import com.target.planogram.models.JwtResponse;
import com.target.planogram.security.JwtHelper;
import com.target.planogram.service.UserService;
import lombok.extern.java.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@Log
public class AuthControllerTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private UserService userService;

    @InjectMocks
    public AuthController authController;

    private JwtRequest jwtRequest;

    @Test
    public void testLogin() {
        UserDetails userDetails = mock(UserDetails.class);
        jwtRequest = new JwtRequest();
        jwtRequest.setUsername("testuser");
        jwtRequest.setPassword("password");
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtHelper.generateToken(userDetails)).thenReturn("testToken");

        ResponseEntity<JwtResponse> response = authController.login(jwtRequest);
        log.info(response.getBody().getJwtToken());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testToken", response.getBody().getJwtToken());
    }

    @Test
    public void testLoginWithBadCredentials() {
        when(userDetailsService.loadUserByUsername("testuser")).thenThrow(new BadCredentialsException("Invalid Username or Password"));
        jwtRequest = new JwtRequest();
        jwtRequest.setUsername("testuser");
        jwtRequest.setPassword("password");
        try {
            authController.login(jwtRequest);
        } catch (BadCredentialsException e) {
            assertEquals("Invalid Username or Password", e.getMessage());
            log.info(e.getMessage());
        }
    }

    @Test
    public void testCreateUser() {
        User newUser = new User();
        when(userService.createUser(newUser)).thenReturn(newUser);
        log.info(newUser.toString());
        User createdUser = authController.createUser(newUser);
        log.info(createdUser.toString());
        assertEquals(newUser, createdUser);
    }
}