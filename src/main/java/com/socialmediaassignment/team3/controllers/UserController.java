package com.socialmediaassignment.team3.controllers;

import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.mappers.UserMapper;
import com.socialmediaassignment.team3.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @GetMapping
    public List<UserResponseDto> getAllUser() {
        return userMapper.entitiesToDtos(userRepository.findAll());
    }
}
