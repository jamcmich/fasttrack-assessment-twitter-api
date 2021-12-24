package com.socialmediaassignment.team3.controllers;

import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.mappers.TweetMapper;
import com.socialmediaassignment.team3.repositories.TweetRepository;
import com.socialmediaassignment.team3.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweet")
public class TweetController {
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final TweetService tweetService;

    @GetMapping
    public List<TweetResponseDto> getAllTweets() {
        return tweetMapper.entitiesToDtos(tweetRepository.findAll());
    }

    /*
        GET users/@{username}/tweets
        Retrieves all (non-deleted) tweets authored by the user with the given username.
    */
    @GetMapping("/@{username}/tweets")
    public List<TweetResponseDto> getUserTweets(@PathVariable String username) {
        return tweetService.getUserTweets(username);
    }

    /*
        GET users/@{username}/mentions
        Retrieves all (non-deleted) tweets in which the user with the given username is mentioned.
    */
    @GetMapping("/@{username}/mentions")
    public List<TweetResponseDto> getTweetsByMention(@PathVariable String username) {
        return tweetService.getTweetsByMention(username);
    }

    /*
        GET users/@{username}/feed
        Retrieves all (non-deleted) tweets authored by the user with the given username,
        as well as all (non-deleted) tweets authored by users the given user is following.
     */
    @GetMapping("/@{username}/feed")
    public List<TweetResponseDto> getUserFeed(@PathVariable String username) {
        return tweetService.getUserFeed(username);
    }
}
