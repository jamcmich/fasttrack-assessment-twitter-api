package com.socialmediaassignment.team3.services.impl;

import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.mappers.UserMapper;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public List<User> getFollowers(String username) throws Exception {
        User user = userRepository.findByCredentialUsername(username);
        System.out.println(user);

        if (user == null || user.isDeleted()) {
            throw new Exception("The requested username " + username + " does not exist!");
        }
        Set<User> followers = user.getFollowers();
        System.out.println(followers);

        List<User> result = new ArrayList<>();
        for (User follower : followers) {
//            // Determine active users.
//            if (!follower.isDeleted()) {
//                result.add(follower);
//            }
//            System.out.println("The user " + username + " is no longer active!");
            result.add(follower);
        }

        System.out.println(result);

        return result;
    }
}
