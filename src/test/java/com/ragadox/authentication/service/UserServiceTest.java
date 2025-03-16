package com.ragadox.authentication.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ragadox.authentication.dto.UserDTO;
import com.ragadox.authentication.entity.UserEntity;
import com.ragadox.authentication.exceptions.BadRequestException;
import com.ragadox.authentication.exceptions.ResourceAlreadyExists;
import com.ragadox.authentication.repository.UserRepository;
import com.ragadox.authentication.utils.JwtUtils;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private JwtUtils jwtUtils;

  @InjectMocks
  private UserService userService;

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void test_createUser_ThrowsBadRequestException_WhenPasswordIsNull() {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("username");
    userDTO.setEmail("email");

    assertThrows(BadRequestException.class, () -> userService.createUser(userDTO));
  }

  @Test
  void createUser_ThrowsResourceAlreadyExists_WhenUsernameIsTaken() {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("username");
    userDTO.setEmail("email");
    userDTO.setPassword("password");

    when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

    assertThrows(ResourceAlreadyExists.class, () -> userService.createUser(userDTO));
  }

  @Test
  void createUser_ThrowsResourceAlreadyExists_WhenEmailIsTaken() {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("username");
    userDTO.setEmail("email");
    userDTO.setPassword("password");

    when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(true);

    assertThrows(ResourceAlreadyExists.class, () -> userService.createUser(userDTO));
  }

  @Test
  void createUser_ShouldReturnUserDTO_WhenValidInput() {

    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("username");
    userDTO.setEmail("email");
    userDTO.setPassword("password");

    UserEntity userEntity = new UserEntity(userDTO);
    userEntity.setId(1L);
    userEntity.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));

    when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(false);
    when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(false);
    when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

    UserDTO createdUser = userService.createUser(userDTO);

    assertNotNull(createdUser);
    assertNotNull(createdUser.getId());
    assertEquals(userDTO.getUsername(), createdUser.getUsername());
  }

  @Test
  void getToken_ShouldReturnToken() throws JsonProcessingException {
    UserDTO userDTO = new UserDTO();
    userDTO.setId(1L);
    userDTO.setUsername("username");
    userDTO.setEmail("email");
    String mock_token = "mocked_jwt_token";
    when(jwtUtils.generateToken(userDTO)).thenReturn(mock_token);

    String token = userService.getToken(userDTO);

    assertNotNull(token);
    assertEquals(mock_token, token);
  }

  @Test
  void verifyCredentials_ThrowsJWTAuthenticationException_WhenUserNotFound() {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("username");
    userDTO.setPassword("password");

    when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());

    assertThrows(BadCredentialsException.class, () -> userService.verifyCredentials(userDTO));
  }

  @Test
  void verifyCredentials_ThrowsBadCredentialsException_WhenPasswordIsIncorrect() {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("username");
    userDTO.setPassword("incorrect_password");

    UserEntity userEntity = new UserEntity(userDTO);
    userEntity.setPasswordHash(passwordEncoder.encode("password"));

    when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(userEntity));

    assertThrows(BadCredentialsException.class, () -> userService.verifyCredentials(userDTO));
  }

  @Test
  void verifyCredentials_ShouldReturnUserDTO_WhenValidCredentials() {
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("username");
    userDTO.setPassword("password");

    UserEntity userEntity = new UserEntity(userDTO);
    userEntity.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));

    when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(userEntity));

    UserDTO verifiedUser = userService.verifyCredentials(userDTO);

    assertNotNull(verifiedUser);
    assertEquals(userDTO.getUsername(), verifiedUser.getUsername());
  }
}
