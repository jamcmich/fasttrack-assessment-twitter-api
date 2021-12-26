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
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        if (tweetOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet not found");
        return tweetMapper.entityToDto(tweetOptional.get());
    }

    @Override
    public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
        User author = _getUserByUsername(tweetRequestDto.getCredentials().getUsername());
        if (author == null || author.isDeleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        Tweet tweet = new Tweet();
        tweet.setAuthor(author);
        tweet.setContent(tweetRequestDto.getContent());
        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweet));
    }

    @Override
    public void likeTweetById(Long id, Credential credential) {
        User user = _getUserByUsername(credential.getUsername());
        if (user == null || user.isDeleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        if (tweetOptional.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tweet not found");
        Tweet tweet = tweetOptional.get();
        user.addLikedTweet(tweet);
        userRepository.saveAndFlush(user);
    }

    @Override
    public ContextResponseDto getContextForTweet(Long id) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        if (tweetOptional.isEmpty() || tweetOptional.get().isDeleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tweet not found");
        Tweet tweet = tweetOptional.get();
        ContextResponseDto responseDto = new ContextResponseDto();
        responseDto.setTarget(tweet);
        responseDto.setBefore(_getTweetsBefore(tweet));
        responseDto.setAfter(_getTweetsAfter(tweet));
        return responseDto;
    }

    @Override
    public TweetResponseDto deleteTweetById(Long id, Credential credential) {
        Optional<Tweet> tweetOptional = tweetRepository.findById(id);
        if (tweetOptional.isEmpty() || tweetOptional.get().isDeleted())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tweet not found");

        Tweet tweet = tweetOptional.get();
        User user = _getUserByUsername(credential.getUsername());
        if (user == null || tweet.getAuthor() != user)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        tweet.setDeleted(true);

        return tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweet));
    }

    private User _getUserByUsername(String username) {
        List<User> userList = userRepository.findByCredentialUsername(username);
        if (userList.size() == 0)
            return null;
        return userList.get(0);
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
