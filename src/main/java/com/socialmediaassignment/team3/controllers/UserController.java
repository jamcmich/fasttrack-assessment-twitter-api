package com.socialmediaassignment.team3.controllers;

import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.dtos.UserRequestDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import com.socialmediaassignment.team3.services.TweetService;
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
    private final TweetService tweetService;

    @GetMapping
    public List<UserResponseDto> getAllUser() {
        return userService.getActiveUsers();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
        return userService.createUser(userRequestDto);
    }

    @GetMapping("/@{username}")
    public UserResponseDto getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PatchMapping("/@{username}")
    public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
        return userService.updateUser(username, userRequestDto);
    }

    @DeleteMapping("/@{username}")
    public UserResponseDto deleteUser(@PathVariable String username, @RequestBody Credential credential) {
        return userService.deleteUser(username, credential);
    }

    @PostMapping("/@{username}/follow")
    public void followUser(@PathVariable String username, @RequestBody Credential credential) {
        userService.followUser(username, credential);
    }

    @PostMapping("/@{username}/unfollow")
    public void unFollowUser(@PathVariable String username, @RequestBody Credential credential) {
        userService.unFollowUser(username, credential);
    }

    /*
        GET users/@{username}/followers
        Retrieves the followers of the user with the given username.
     */
    @GetMapping("/@{username}/followers")
    public List<UserResponseDto> getFollowers(@PathVariable String username) {
        return userService.getFollowers(username);
    }

    /*
        GET users/@{username}/following
        Retrieves the users followed by the user with the given username.
    */
    @GetMapping("/@{username}/following")
    public List<UserResponseDto> getFollowedUsers(@PathVariable String username) {
        return userService.getFollowedUsers(username);
    }

    /*
        GET users/@{username}/tweets
        Retrieves all (non-deleted) tweets authored by the user with the given username.
    */
    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getUserTweets(@PathVariable String username) {
        return tweetService.getUserTweets(username);
    }

    /*
        GET users/@{username}/mentions
        Retrieves all (non-deleted) tweets in which the user with the given username is mentioned.
    */
    @GetMapping("/@{username}/mentions")
    public List<TweetResponseDto> getTweetsByMention(@PathVariable String username) {
        return tweetService.getTweetsByMention(username);
    }

    /*
        GET users/@{username}/feed
        Retrieves all (non-deleted) tweets authored by the user with the given username,
        as well as all (non-deleted) tweets authored by users the given user is following.
     */
    @GetMapping("/@{username}/feed")
    public List<TweetResponseDto> getUserFeed(@PathVariable String username) {
        return tweetService.getUserFeed(username);
    }
}
