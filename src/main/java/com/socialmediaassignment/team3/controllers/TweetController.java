package com.socialmediaassignment.team3.controllers;

import com.socialmediaassignment.team3.dtos.ContextResponseDto;
import com.socialmediaassignment.team3.dtos.TweetRequestDto;
import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import com.socialmediaassignment.team3.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweets")
public class TweetController {
    private final TweetService tweetService;

    @GetMapping
    public List<TweetResponseDto> getAllTweets() {
        return tweetService.getActiveTweets();
    }

    @GetMapping("/{id}")
    public TweetResponseDto getTweetById(@PathVariable Long id) {
        return tweetService.getTweetById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseDto createTweet(@RequestBody TweetRequestDto tweetRequestDto) {
        return tweetService.createTweet(tweetRequestDto);
    }

    @PostMapping("/{id}/like")
    public void likeTweetById(@PathVariable Long id, @RequestBody Credential credential) {
        tweetService.likeTweetById(id, credential);
    }

    @GetMapping("/{id}/context")
    public ContextResponseDto getContextForTweet(@PathVariable Long id) {
        return tweetService.getContextForTweet(id);
    }

    @DeleteMapping("/{id}")
    public TweetResponseDto deleteTweetById(@PathVariable Long id, @RequestBody Credential credential) {
        return tweetService.deleteTweetById(id, credential);
    }

    @PostMapping("/{id}/repost")
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseDto repostTweetById(@PathVariable Long id, @RequestBody Credential credential) {
        return tweetService.repostTweetById(id, credential);
    }

    @GetMapping("/{id}/reposts")
    public List<TweetResponseDto> getRepostOfTweetById(@PathVariable Long id) {
        return tweetService.getRepostOfTweetById(id);
    }

    @PostMapping("/{id}/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public TweetResponseDto replyTweetById(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
        return tweetService.replyTweetById(id, tweetRequestDto);
    }

    @GetMapping("/{id}/replies")
    public List<TweetResponseDto> getReplyToTweetById(@PathVariable Long id) {
        return tweetService.getRepliesToTweetById(id);
    }

    @GetMapping("/{id}/mentions")
    public List<UserResponseDto> getMentionInTweetById(@PathVariable Long id) {
        return tweetService.getMentionInTweetById(id);
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
