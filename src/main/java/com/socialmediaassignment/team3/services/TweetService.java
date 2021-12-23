package com.socialmediaassignment.team3.services;

import com.socialmediaassignment.team3.dtos.TweetRequestDto;
import com.socialmediaassignment.team3.dtos.TweetResponseDto;

import java.util.List;

public interface TweetService {
    List<TweetResponseDto> getActiveTweets();

    TweetResponseDto getTweetById(Long id);

    TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);
}
