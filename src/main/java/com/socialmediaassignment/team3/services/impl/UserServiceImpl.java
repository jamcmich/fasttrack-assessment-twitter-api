package com.socialmediaassignment.team3.services.impl;

import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.mappers.UserMapper;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    // Checks whether a given username exists.
    public Boolean validateUsername(String username) {
        return userRepository.findByCredentialUsername(username) != null;
    }

    // Retrieves a user with the given username.
    public User getUsername(String username) throws Exception {
        User result = userRepository.findByCredentialUsername(username);

        if (result == null || result.isDeleted()) {
            throw new Exception("The requested username " + username + " does not exist!");
        }
        return result;
    }
}
