package com.socialmediaassignment.team3.services.impl;


import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.dtos.UserRequestDto;
import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.mappers.TweetMapper;
import com.socialmediaassignment.team3.repositories.TweetRepository;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetMapper tweetMapper;
    private final TweetRepository tweetRepository;

    private final UserRepository userRepository;

    /*
        GET users/@{username}/tweets
        Retrieves all (non-deleted) tweets authored by the user with the given username.
    */
    @Override
    public List<TweetResponseDto> getUserTweets(String username) {
        User user = _getUserByUsername(username);

        if (!isActive(user))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");

        Set<Tweet> tweets = user.getTweets();

        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (!tweet.isDeleted())
                result.add(tweet);
        }
        result.sort(Comparator.comparing(Tweet::getPosted));
        Collections.reverse(result); // sort list recent -> older

        // test
        for (Tweet tweet : result) {
            System.out.println(tweet.getPosted());
        }

        return tweetMapper.entitiesToDtos(result);
    }

    /*
        GET users/@{username}/mentions
        Retrieves all (non-deleted) tweets in which the user with the given username is mentioned.
    */
    @Override
    public List<TweetResponseDto> getTweetsByMention(String username) {
        User user = _getUserByUsername(username);

        if (!isActive(user))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");

        List<Tweet> tweets = tweetRepository.findAll();
        System.out.println(tweets);

        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (!tweet.isDeleted() && tweet.getContent().contains("@" + username))
                result.add(tweet);
        }
        result.sort(Comparator.comparing(Tweet::getPosted));
        Collections.reverse(result); // sort list recent -> older

        // test
        for (Tweet tweet : result) {
            System.out.println(tweet.getPosted() + ", " + tweet.getContent());
        }

        return tweetMapper.entitiesToDtos(result);
    }

    // Helper methods
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
