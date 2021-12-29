package com.socialmediaassignment.team3.services;

import com.socialmediaassignment.team3.dtos.ContextResponseDto;
import com.socialmediaassignment.team3.dtos.TweetRequestDto;
import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.embeddable.Credential;

import java.util.List;

public interface TweetService {
    List<TweetResponseDto> getActiveTweets();

    TweetResponseDto getTweetById(Long id);

    TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

    void likeTweetById(Long id, Credential credential);

    ContextResponseDto getContextForTweet(Long id);

    TweetResponseDto deleteTweetById(Long id, Credential credential);

    TweetResponseDto repostTweetById(Long id, Credential credential);

    List<TweetResponseDto> getRepostOfTweetById(Long id);

    TweetResponseDto replyTweetById(Long id, TweetRequestDto tweetRequestDto);

    List<TweetResponseDto> getRepliesToTweetById(Long id);

    List<UserResponseDto> getMentionInTweetById(Long id);

    List<UserResponseDto> getLikeForTweet(Long id);
}
