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

    /*
        GET users/@{username}/feed
        Retrieves all (non-deleted) tweets authored by the user with the given username,
        as well as all (non-deleted) tweets authored by users the given user is following.
    */
    @Override
    public List<TweetResponseDto> getUserFeed(String username) {
        User user = _getUserByUsername(username);

        if (!isActive(user))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");

        List<Tweet> result = new ArrayList<>();
        Set<User> following = user.getFollowing();

        List<Tweet> tweets = tweetRepository.findAll();
        for (Tweet tweet : tweets) {
            // if the tweet's author's username == requested username
            // OR if tweet's author is in the list of people that the requested user is following
            if (!tweet.isDeleted()) {
                if (Objects.equals(tweet.getAuthor().getCredential().getUsername(), username) || following.contains(tweet.getAuthor())) {
                    result.add(tweet);
                }
            }
        }
        result.sort(Comparator.comparing(Tweet::getPosted));
        Collections.reverse(result); // sort list recent -> older

        for (Tweet r : result) {
            System.out.println("AUTHOR: " + r.getAuthor());
            System.out.println("FOLLOWING: " + r.getAuthor().getFollowing());
            System.out.println("CONTENT: " + r.getContent());
            System.out.println("DATE POSTED: " + r.getPosted());
        }

        return tweetMapper.entitiesToDtos(result);
    }

    /*
        GET tweets/{id}/context
        Retrieves the context of the tweet with the given id.
        If that tweet is deleted or otherwise doesn't exist,
        an error should be sent in lieu of a response.
    */
    // TODO: Implement getTweetContext() method
    @Override
    public List<TweetResponseDto> getTweetContext(Long id) {
        return null;
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
