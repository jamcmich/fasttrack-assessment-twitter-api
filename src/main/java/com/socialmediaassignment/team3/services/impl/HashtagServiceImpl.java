package com.socialmediaassignment.team3.services.impl;

import com.socialmediaassignment.team3.dtos.HashtagResponseDto;
import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.entities.Hashtag;
import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.exceptions.BadRequestException;
import com.socialmediaassignment.team3.mappers.HashtagMapper;
import com.socialmediaassignment.team3.mappers.TweetMapper;
import com.socialmediaassignment.team3.repositories.HashtagRepository;
import com.socialmediaassignment.team3.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagMapper hashtagMapper;
    private final HashtagRepository hashtagRepository;
    private final TweetMapper tweetMapper;

    @Override
    public List<HashtagResponseDto> getAllHashtags() {
        return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
    }

    @Override
    public List<TweetResponseDto> getTweetByTag(String label) {
        Optional<Hashtag> hashtagOptional = hashtagRepository.findByLabel(label);
        if (hashtagOptional.isEmpty())
            throw new BadRequestException("Invalid label");

        Set<Tweet> tweets = hashtagOptional.get().getTweets();

        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (!tweet.isDeleted() && tweet.getHashtags().contains(hashtagOptional) && tweet.getContent().contains(hashtagOptional.get().getLabel())) {
                result.add(tweet);
            }
        }
        result.sort(Comparator.comparing(Tweet::getPosted));
        Collections.reverse(result); // sort list recent -> older

        return tweetMapper.entitiesToDtos(hashtagOptional.get().getTweets().stream().filter(t -> !t.isDeleted()).collect(Collectors.toList()));
    }
}
