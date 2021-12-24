package com.socialmediaassignment.team3.services;

import com.socialmediaassignment.team3.dtos.UserRequestDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.embeddable.Credential;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getActiveUsers ();

    UserResponseDto createUser(UserRequestDto userRequestDto);

    UserResponseDto getUserByUsername(String username);

    Boolean validateUsername(String username);

    UserResponseDto updateUser(String username, UserRequestDto userRequestDto);

    UserResponseDto deleteUser(String username, Credential credential);

    void followUser(String username, Credential credential);

    void unFollowUser(String username, Credential credential);

    List<UserResponseDto> getFollowers(String username);

    List<UserResponseDto> getFollowedUsers(String username);
}
