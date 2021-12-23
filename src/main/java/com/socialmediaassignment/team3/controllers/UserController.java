package com.socialmediaassignment.team3.controllers;

import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.mappers.UserMapper;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAllUser() {
        return userMapper.entitiesToDtos(userRepository.findAll());
    }

    // Checks whether a given username exists.
    @GetMapping("/validate/username/exists/@{username}")
    public Boolean validateUsername(@PathVariable String username) {
        return userService.validateUsername(username);
    }

    // Retrieves a user with the given username.
    @GetMapping("/@{username}")
    public User getUsername(@PathVariable String username) throws Exception {
        return userService.getUsername(username);
    }

    // Retrieves the followers of the user with the given username.
    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getFollowers(@PathVariable String username) throws Exception {
        return userService.getFollowers(username);
    }

    // Retrieves all (non-deleted) tweets authored by the user with the given username.
    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getUserTweets(@PathVariable String username) throws Exception {
        return userService.getUserTweets(username);
    }
}
