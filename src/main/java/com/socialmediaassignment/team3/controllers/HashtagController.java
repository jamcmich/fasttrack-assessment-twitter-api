package com.socialmediaassignment.team3.controllers;

import com.socialmediaassignment.team3.dtos.HashtagResponseDto;
import com.socialmediaassignment.team3.mappers.HashtagMapper;
import com.socialmediaassignment.team3.repositories.HashtagRepository;
import com.socialmediaassignment.team3.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hashtag")
public class HashtagController {
    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;
    private final HashtagService hashtagService;

    @GetMapping
    public List<HashtagResponseDto> getAllHashtags() {
        return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
    }

    // Checks whether a given hashtag exists.
    @GetMapping("/validate/tag/exists/{label}")
    public Boolean getHashtag(@PathVariable String label) {
        return hashtagService.getHashtag(label);
    }
}
