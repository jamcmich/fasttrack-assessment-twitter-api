package com.socialmediaassignment.team3.services;

<<<<<<< HEAD
import com.socialmediaassignment.team3.dtos.UserCreateDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getActiveUsers ();

    UserResponseDto createUser(UserCreateDto userCreateDto);

    UserResponseDto getUserByUsername(String username);
=======
public interface UserService {
    Boolean validateUsername(String username);
>>>>>>> a458223bea0a77de757914629001761ebecd1df2
}
