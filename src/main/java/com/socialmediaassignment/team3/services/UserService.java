package com.socialmediaassignment.team3.services;

import com.socialmediaassignment.team3.dtos.UserCreateDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getActiveUsers ();

    UserResponseDto createUser(UserCreateDto userCreateDto);

    UserResponseDto getUserByUsername(String username);

    Boolean validateUsername(String username);
}
