package com.socialmediaassignment.team3.services;

import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.User;

import java.util.List;

public interface UserService {
    Boolean validateUsername(String username);

    User getUsername(String username) throws Exception;

    List<UserResponseDto> getFollowers(String username) throws Exception;

    List<TweetResponseDto> getUserTweets(String username) throws Exception;
}
