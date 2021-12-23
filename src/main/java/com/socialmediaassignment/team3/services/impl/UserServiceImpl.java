package com.socialmediaassignment.team3.services.impl;

<<<<<<< HEAD
import com.socialmediaassignment.team3.dtos.UserCreateDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
=======
>>>>>>> a458223bea0a77de757914629001761ebecd1df2
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.mappers.UserMapper;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.UserService;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
=======
import org.springframework.stereotype.Service;
>>>>>>> a458223bea0a77de757914629001761ebecd1df2

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
<<<<<<< HEAD
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public List<UserResponseDto> getActiveUsers() {
        List<User> userList = userRepository.findAll();
        return userMapper.entitiesToDtos(userList.stream().filter( user -> !user.isDeleted()).collect(Collectors.toList()));
    }

    @Override
    public UserResponseDto createUser(UserCreateDto userCreateDto) {
        User user = _getUserByUsername(userCreateDto.getCredential().getUsername());
        if (user == null)
            user = userMapper.createDtoToEntity(userCreateDto);
        else if (user.isDeleted()) {
            user.setCredential(userCreateDto.getCredential());
            user.setProfile(userCreateDto.getProfile());
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

    private boolean existsUsername(String username) {
        return userRepository.findByCredentialUsername(username).size() > 0;
    }

    private User _getUserByUsername(String username) {
        List<User> userList = userRepository.findByCredentialUsername(username);
        if (userList.size() == 0)
            return null;
        return userList.get(0);
=======

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    // Checks whether a given username exists.
    public Boolean validateUsername(String username) {
        User user = userRepository.findByCredentialUsername(username);

        return user != null;
>>>>>>> a458223bea0a77de757914629001761ebecd1df2
    }
}
