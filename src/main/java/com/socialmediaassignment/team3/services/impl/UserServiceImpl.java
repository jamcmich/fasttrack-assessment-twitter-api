package com.socialmediaassignment.team3.services.impl;

import com.socialmediaassignment.team3.dtos.UserRequestDto;
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
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User user = _getUserByUsername(userRequestDto.getCredential().getUsername());
        if (user == null)
            user = userMapper.createDtoToEntity(userRequestDto);
        else if (user.isDeleted()) {
            user = _setCredentialAndProfile(user, userRequestDto);
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be unique");
        return userMapper.entityToDto(userRepository.saveAndFlush(user));
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = _getUserByUsername(username);
        if (user == null || user.isDeleted())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with username '" + username + "' not found");
        return userMapper.entityToDto(user);
    }

    // Checks whether a given username exists.
    @Override
    public Boolean validateUsername(String username) {
        return _getUserByUsername(username) != null;
    }

    @Override
    public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
        User toUpdate = _getUserByUsername(username);
        if (toUpdate == null || toUpdate.isDeleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        toUpdate = _setCredentialAndProfile(toUpdate, userRequestDto);
        toUpdate.getCredential().setUsername(username);
        return userMapper.entityToDto(userRepository.saveAndFlush(toUpdate));
    }

    // Auxiliary functions

    private boolean existsUsername(String username) {
        return userRepository.findByCredentialUsername(username).size() > 0;
    }

    private User _getUserByUsername(String username) {
        List<User> userList = userRepository.findByCredentialUsername(username);
        if (userList.size() == 0)
            return null;
        return userList.get(0);
    }

    private User _setCredentialAndProfile (User user, UserRequestDto userRequestDto) {
        user.setCredential(userRequestDto.getCredential());
        user.setProfile(userRequestDto.getProfile());
        return user;
    }

}
