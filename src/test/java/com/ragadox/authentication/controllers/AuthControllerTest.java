package com.ragadox.authentication.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ragadox.authentication.dto.UserDTO;
import com.ragadox.authentication.service.UserService;

public class AuthControllerTest {

  @Mock
  private UserService userService;

  @InjectMocks
  private AuthController authController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @SuppressWarnings("null")
  @Test
  void signUp_WhenCalled_ReturnsUserDTO() {

    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("username");
    userDTO.setEmail("email");
    userDTO.setPassword("password");

    when(userService.createUser(userDTO)).thenReturn(userDTO);

    var response = authController.signUp(userDTO);

    assertNotNull(response);
    assertEquals(response.getStatusCode(), HttpStatus.OK);
    assertNotNull(response.getBody());
    assertEquals(userDTO, response.getBody().getData());
  }

  @SuppressWarnings("null")
  @Test
  void signIn_WhenCalled_ReturnsUserDTO() throws JsonProcessingException {

    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("username");
    userDTO.setEmail("email");
    userDTO.setPassword("password");
    String mock_token = "mock_token";

    when(userService.verifyCredentials(userDTO)).thenReturn(userDTO);
    when(userService.getToken(userDTO)).thenReturn(mock_token);

    var response = authController.signIn(userDTO);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(response.getBody().getData(), userDTO);
  }

}
