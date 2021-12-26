package com.socialmediaassignment.team3.services.impl;


import com.socialmediaassignment.team3.dtos.ContextResponseDto;
import com.socialmediaassignment.team3.dtos.TweetRequestDto;
import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import com.socialmediaassignment.team3.mappers.TweetMapper;
import com.socialmediaassignment.team3.repositories.TweetRepository;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetMapper tweetMapper;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Override
    public List<TweetResponseDto> getActiveTweets() {
        return tweetMapper.entitiesToDtos(tweetRepository.findAll().stream().filter(tweet -> !tweet.isDeleted()).collect(Collectors.toList()));
    }

    @Override
    public TweetResponseDto getTweetById(Long id) {
        return tweetMapper.entityToDto(_getActiveTweetById(id));
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        User author = _authorizeCredential(tweetRequestDto.getCredentials());
        Tweet tweet = new Tweet();
        tweet.setAuthor(author);
        tweet.setContent(tweetRequestDto.getContent());
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweet));
    }

    @Override
    public void likeTweetById(Long id, Credential credential) {
        User user = _authorizeCredential(credential);
        Tweet tweet = _getActiveTweetById(id);
        user.addLikedTweet(tweet);
        userRepository.saveAndFlush(user);
    }

    @Override
    public ContextResponseDto getContextForTweet(Long id) {
        Tweet tweet = _getActiveTweetById(id);
        ContextResponseDto responseDto = new ContextResponseDto();
        responseDto.setTarget(tweet);
        responseDto.setBefore(_getTweetsBefore(tweet));
        responseDto.setAfter(_getTweetsAfter(tweet));
        return responseDto;
    }

    @Override
    public TweetResponseDto deleteTweetById(Long id, Credential credential) {
        Tweet tweet = _getActiveTweetById(id);
        User user = _authorizeCredential(credential);
        if (user != tweet.getAuthor())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        tweet.setDeleted(true);

        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweet));
    }

    @Override
    public TweetResponseDto repostTweetById(Long id, Credential credential) {
        User user = _authorizeCredential(credential);
        Tweet originalTweet = _getActiveTweetById(id);
        Tweet repostTweet = new Tweet();
        repostTweet.setAuthor(user);
        repostTweet.setRepostOf(originalTweet);
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(repostTweet));
    }

    private User _authorizeCredential(Credential credential) {
        Optional<User> userOptional = userRepository.findOneByCredential(credential);
        if (userOptional.isEmpty() || userOptional.get().isDeleted())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        return userOptional.get();
    }

    private Tweet _getActiveTweetById(Long id) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        if (tweetOptional.isEmpty() || tweetOptional.get().isDeleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tweet not found");
        return tweetOptional.get();
    }

    private List<Tweet> _getTweetsBefore(Tweet tweet) {
        List<Tweet> tweetList = new ArrayList<>();
        Tweet actualTweet = tweet;
        while (actualTweet.getInReplyTo() != null) {
            actualTweet = actualTweet.getInReplyTo();
            if (!actualTweet.isDeleted())
                tweetList.add(actualTweet);
        }
        return tweetList;
    }

    private List<Tweet> _getTweetsAfter(Tweet tweet) {
        List<Tweet> tweetList = new ArrayList<>();
        Queue<Tweet> tweetQueue = new LinkedList<>();
        tweetQueue.addAll(tweet.getReplies());

        while (tweetQueue.size() > 0) {
            Tweet actualTweet = tweetQueue.poll();
            if (!actualTweet.isDeleted())
                tweetList.add(actualTweet);
            tweetQueue.addAll(actualTweet.getReplies());
        }
        return tweetList;
    }
}
