package com.socialmediaassignment.team3.services.impl;

import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.mappers.UserMapper;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // Retrieves the followers of the user with the given username.
    public Set<User> getFollowers(String username) throws Exception {
        User user = userRepository.findByCredentialUsername(username);

        if (user == null || user.isDeleted()) {
            throw new Exception("The requested username " + username + " does not exist!");
        }
        Set<User> followers = user.getFollowers();

        Set<User> result = new HashSet<>();
        for (User follower : followers) {
            // Determine active users.
            if (!follower.isDeleted()) {
                result.add(follower);
            }
            System.out.println("The user " + username + " is no longer active!");
        }

        return result;
    }
}
