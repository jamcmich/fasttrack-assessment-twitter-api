package com.socialmediaassignment.team3.services.impl;

import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.mappers.TweetMapper;
import com.socialmediaassignment.team3.mappers.UserMapper;
import com.socialmediaassignment.team3.repositories.TweetRepository;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private final TweetMapper tweetMapper;
    private final TweetRepository tweetRepository;

    // Checks whether a given username exists.
    public Boolean validateUsername(String username) {
        return userRepository.findByCredentialUsername(username) != null;
    }

    // Retrieves a user with the given username.
    public User getUsername(String username) throws Exception {
        User result = userRepository.findByCredentialUsername(username);

        if (result == null || result.isDeleted()) {
            throw new Exception("The requested username " + username + " does not exist!");
        }
        return result;
    }

    // [NOT WORKING] Retrieves the followers of the user with the given username.
    public List<UserResponseDto> getFollowers(String username) throws Exception {
        User user = userRepository.findByCredentialUsername(username);

        if (user == null || user.isDeleted()) {
            throw new Exception("The requested username " + username + " does not exist!");
        }

        List<User> result = new ArrayList<>();

        Set<User> followers = user.getFollowers();
        for (User follower : followers) {
            // Determine active users.
            if (!follower.isDeleted()) {
                result.add(follower);
            }
            System.out.println("The user " + username + " is no longer active!");
        }

        return userMapper.entitiesToDtos(result);
    }

    // [NOT WORKING] Retrieves all (non-deleted) tweets authored by the user with the given username.
    public List<TweetResponseDto> getUserTweets(String username) throws Exception {
        User user = userRepository.findByCredentialUsername(username);

//        if (user == null || user.isDeleted()) {
//            throw new Exception("The requested username " + username + " does not exist!");
//        }

        List<Tweet> result = new ArrayList<>();

        Set<Tweet> tweets = user.getTweets();
        for (Tweet tweet : tweets) {
            if (!tweet.isDeleted()) {
                result.add(tweet);
            }
            System.out.println("The tweet " + tweet + " no longer exists!");
        }

        return tweetMapper.entitiesToDtos(result);
    }
}
