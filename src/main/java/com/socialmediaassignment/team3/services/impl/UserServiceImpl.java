package com.socialmediaassignment.team3.services.impl;

import com.socialmediaassignment.team3.dtos.UserRequestDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import com.socialmediaassignment.team3.mappers.UserMapper;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /*
        GET users
        Retrieves all active (non-deleted) users as an array.
    */
    @Override
    public List<UserResponseDto> getActiveUsers() {
        List<User> userList = userRepository.findAll();
        return userMapper.entitiesToDtos(userList.stream().filter( user -> !user.isDeleted()).collect(Collectors.toList()));
    }

    /*
        POST users
        Creates a new user.
    */
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

    /*
        GET users/@{username}
        Retrieves a user with the given username.
    */
    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = _getUserByUsername(username);
        if (user == null || user.isDeleted())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with username '" + username + "' not found");
        return userMapper.entityToDto(user);
    }

    /*
        GET validate/username/exists/@{username}
        Checks whether a given username exists.
    */
    @Override
    public Boolean validateUsername(String username) {
        return _getUserByUsername(username) != null;
    }

    /*
        PATCH users/@{username}
        Updates the profile of a user with the given username.
    */
    @Override
    public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
        User toUpdate = _getUserByUsername(username);
        if (toUpdate == null || toUpdate.isDeleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        toUpdate = _setCredentialAndProfile(toUpdate, userRequestDto);
        toUpdate.getCredential().setUsername(username);
        return userMapper.entityToDto(userRepository.saveAndFlush(toUpdate));
    }

    /*
        DELETE users/@{username}
        "Deletes" a user with the given username.
    */
    @Override
    public UserResponseDto deleteUser(String username, Credential credential) {
        User toDelete = _getUserByUsername(username);
        if (toDelete == null || toDelete.isDeleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        toDelete.setDeleted(true);
        return userMapper.entityToDto(userRepository.saveAndFlush(toDelete));
    }

    /*
        POST users/@{username}/follow
        Subscribes the user whose credentials are provided
        by the request body to the user whose username is given in the url.
    */
    @Override
    public void followUser(String username, Credential credential) {
        User toBeFollowed = _getUserByUsername(username);
        User follower = _getUserByUsername(credential.getUsername());
        if (!isActive(toBeFollowed) || !isActive(follower))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        if (follower.getFollowing().contains(toBeFollowed))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already following");
        follower.addFollowing(toBeFollowed);
        userRepository.saveAndFlush(follower);
        userRepository.saveAndFlush(toBeFollowed);
    }

    /*
        POST users/@{username}/unfollow
        Unsubscribes the user whose credentials are provided
        by the request body from the user whose username is given in the url.
    */
    @Override
    public void unFollowUser(String username, Credential credential) {
        User toBeFollowed = _getUserByUsername(username);
        User follower = _getUserByUsername(credential.getUsername());
        if (!isActive(toBeFollowed) || !isActive(follower))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        if (!follower.getFollowing().contains(toBeFollowed))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not following");
        follower.removeFollowing(toBeFollowed);
        userRepository.saveAndFlush(follower);
        userRepository.saveAndFlush(toBeFollowed);
    }

    /*
        GET users/@{username}/followers
        Retrieves the followers of the user with the given username.
     */
    @Override
    public List<UserResponseDto> getFollowers(String username) {
        User user = _getUserByUsername(username);

        if (!isActive(user))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");

        Set<User> followers = user.getFollowers();

        List<User> result = new ArrayList<>();
        for (User follower : followers) {
            if (!follower.isDeleted())
                result.add(follower);
        }
        return userMapper.entitiesToDtos(result);
    }

    /*
        GET users/@{username}/following
        Retrieves the users followed by the user with the given username.
    */
    @Override
    public List<UserResponseDto> getFollowedUsers(String username) {
        User user = _getUserByUsername(username);

        if (!isActive(user))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");

        Set<User> following = user.getFollowing();

        List<User> result = new ArrayList<>();
        for (User follower : following) {
            if (!follower.isDeleted())
                result.add(follower);
        }
        return userMapper.entitiesToDtos(result);
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

    private boolean isActive(User user) {
        return user != null && !user.isDeleted();
    }

}
