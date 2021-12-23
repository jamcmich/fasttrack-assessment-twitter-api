package com.socialmediaassignment.team3.services.impl;

import com.socialmediaassignment.team3.dtos.UserCreateDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.mappers.UserMapper;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public List<UserResponseDto> getActiveUsers() {
        List<User> userList = userRepository.findAll();
        return userMapper.entitiesToDtos(userList.stream().filter( user -> !user.isDeleted()).collect(Collectors.toList()));
    }

    @Override
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        User toSave = userMapper.createDtoToEntity(userCreateDto);
        if (existsUsername(toSave.getCredential().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be unique");
        }
        return userMapper.entityToDto(userRepository.saveAndFlush(toSave));
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        if (existsUsername(username)) {
            return userMapper.entityToDto(_getUserByUsername(username));
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with username '" + username + "' not found");
    }

    private boolean existsUsername(String username) {
        return userRepository.findByCredentialUsername(username).size() > 0;
    }

    private User _getUserByUsername(String username) throws IndexOutOfBoundsException{
        return userRepository.findByCredentialUsername(username).get(0);
    }
}
