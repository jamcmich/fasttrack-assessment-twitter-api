package com.socialmediaassignment.team3.controllers;

import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.mappers.TweetMapper;
import com.socialmediaassignment.team3.repositories.TweetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tweet")
public class TweetController {
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;

    @GetMapping
    public List<TweetResponseDto> getAllTweets() {
        return tweetMapper.entitiesToDtos(tweetRepository.findAll());
    }
}
