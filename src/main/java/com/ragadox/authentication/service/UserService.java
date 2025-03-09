package com.ragadox.authentication.service;

import com.ragadox.authentication.dto.UserDTO;
import com.ragadox.authentication.entity.UserEntity;
import com.ragadox.authentication.exceptions.ResourceAlreadyExists;
import com.ragadox.authentication.exceptions.ResourceNotFound;
import com.ragadox.authentication.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    public UserEntity createUser(UserDTO userDTO) {
        UserEntity userEntity=new UserEntity(userDTO);
        Boolean isUsernameTaken=userRepository.existsByUsername(userEntity.getUsername());
        Boolean isEmailTaken=userRepository.existsByEmail(userEntity.getEmail());
        if(isUsernameTaken || isEmailTaken){
            throw new ResourceAlreadyExists(UserEntity.class);
        }
        return userRepository.save(userEntity);
    }

    public UserEntity getUserByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFound(UserEntity.class));
    }
}
