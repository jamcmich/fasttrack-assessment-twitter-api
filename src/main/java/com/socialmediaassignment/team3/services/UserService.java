package com.socialmediaassignment.team3.services;

import com.socialmediaassignment.team3.entities.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    Boolean validateUsername(String username);

    User getUsername(String username) throws Exception;

    Set<User> getFollowers(String username) throws Exception;
}
