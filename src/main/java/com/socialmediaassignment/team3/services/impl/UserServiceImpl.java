package com.socialmediaassignment.team3.services.impl;

import com.socialmediaassignment.team3.dtos.UserRequestDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import com.socialmediaassignment.team3.exceptions.BadRequestException;
import com.socialmediaassignment.team3.exceptions.NotFoundException;
import com.socialmediaassignment.team3.exceptions.UnauthorizedException;
import com.socialmediaassignment.team3.mappers.UserMapper;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
            throw new BadRequestException("Username must be unique");
        return userMapper.entityToDto(userRepository.saveAndFlush(user));
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = _getUserByUsername(username);
        if (user == null || user.isDeleted())
            throw new NotFoundException("User with username '" + username + "' not found");
        return userMapper.entityToDto(user);
    }

    @Override
    public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
        User toUpdate = _authorizeCredential(userRequestDto.getCredential());

        toUpdate = _setCredentialAndProfile(toUpdate, userRequestDto);
        toUpdate.getCredential().setUsername(username);
        return userMapper.entityToDto(userRepository.saveAndFlush(toUpdate));
    }

    @Override
    public UserResponseDto deleteUser(String username, Credential credential) {
        User toDelete = _authorizeCredential(credential);
        toDelete.setDeleted(true);
        return userMapper.entityToDto(userRepository.saveAndFlush(toDelete));
    }

    @Override
    public void followUser(String username, Credential credential) {
        User toBeFollowed = _getUserByUsername(username);
        User follower = _authorizeCredential(credential);
        if (!isActive(toBeFollowed))
            throw new BadRequestException("User not found");
        if (follower.getFollowing().contains(toBeFollowed))
            throw new BadRequestException("Already following");
        follower.addFollowing(toBeFollowed);
        userRepository.saveAndFlush(follower);
        userRepository.saveAndFlush(toBeFollowed);
    }

    @Override
    public void unFollowUser(String username, Credential credential) {
        User toBeUnfollowed = _getUserByUsername(username);
        User follower = _authorizeCredential(credential);
        if (!isActive(toBeUnfollowed))
            throw new BadRequestException("User not found");
        if (!follower.getFollowing().contains(toBeUnfollowed))
            throw new BadRequestException("Not following");
        follower.removeFollowing(toBeUnfollowed);
        userRepository.saveAndFlush(follower);
        userRepository.saveAndFlush(toBeUnfollowed);
    }

    // Auxiliary functions
    private User _getUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByCredentialUsername(username);
        if (userOptional.isEmpty())
            return null;
        return userOptional.get();
    }

    private User _setCredentialAndProfile (User user, UserRequestDto userRequestDto) {
        user.setCredential(userRequestDto.getCredential());
        user.setProfile(userRequestDto.getProfile());
        return user;
    }

    private User _authorizeCredential(Credential credential) {
        Optional<User> userOptional = userRepository.findOneByCredential(credential);
        if (userOptional.isEmpty() || userOptional.get().isDeleted())
            throw new UnauthorizedException("Bad credentials");
        return userOptional.get();
    }

    private boolean isActive(User user) {
        return user != null && !user.isDeleted();
    }

}
