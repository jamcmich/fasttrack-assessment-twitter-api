package com.socialmediaassignment.team3.services.impl;


import com.socialmediaassignment.team3.mappers.TweetMapper;
import com.socialmediaassignment.team3.repositories.TweetRepository;
import com.socialmediaassignment.team3.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {

    private final TweetMapper tweetMapper;
    private final TweetRepository tweetRepository;
}
