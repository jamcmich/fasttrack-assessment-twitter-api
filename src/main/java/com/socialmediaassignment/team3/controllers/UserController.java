package com.socialmediaassignment.team3.controllers;

import com.socialmediaassignment.team3.dtos.UserRequestDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import com.socialmediaassignment.team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /*
        GET users
        Retrieves all active (non-deleted) users as an array.
    */
    @GetMapping
    public List<UserResponseDto> getAllUser() {
        return userService.getActiveUsers();
    }

    /*
        POST users
        Creates a new user.
    */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    /*
        GET users/@{username}
        Retrieves a user with the given username.
    */
    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    /*
        PATCH users/@{username}
        Updates the profile of a user with the given username.
    */
    @PatchMapping("/@{username}")
    public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(username, userRequestDto);
    }

    /*
        DELETE users/@{username}
        "Deletes" a user with the given username.
    */
    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(@PathVariable String username, @RequestBody Credential credential) {
        return userService.deleteUser(username, credential);
    }

    /*
        POST users/@{username}/follow
        Subscribes the user whose credentials are provided
        by the request body to the user whose username is given in the url.
    */
    @PostMapping("/@{username}/follow")
    public void followUser(@PathVariable String username, @RequestBody Credential credential) {
        userService.followUser(username, credential);
    }

    /*
        POST users/@{username}/unfollow
        Unsubscribes the user whose credentials are provided
        by the request body from the user whose username is given in the url.
    */
    @PostMapping("/@{username}/unfollow")
    public void unFollowUser(@PathVariable String username, @RequestBody Credential credential) {
        userService.unFollowUser(username, credential);
    }

    /*
        GET validate/username/exists/@{username}
        Checks whether a given username exists.
    */
    @GetMapping("/validate/username/exists/@{username}")
    public Boolean validateUsername(@PathVariable String username) {
        return userService.validateUsername(username);
    }

    /*
        GET users/@{username}/followers
        Retrieves the followers of the user with the given username.
     */
    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getFollowers(String username) {
        return userService.getFollowers(username);
    }
}
