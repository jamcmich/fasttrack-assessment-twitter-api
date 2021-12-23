package com.socialmediaassignment.team3.services;

import com.socialmediaassignment.team3.entities.User;

public interface UserService {
    Boolean validateUsername(String username);

    User getUsername(String username) throws Exception;
}
