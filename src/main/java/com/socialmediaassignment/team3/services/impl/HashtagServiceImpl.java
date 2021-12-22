package com.socialmediaassignment.team3.services.impl;

import com.socialmediaassignment.team3.entities.Hashtag;
import com.socialmediaassignment.team3.mappers.HashtagMapper;
import com.socialmediaassignment.team3.repositories.HashtagRepository;
import com.socialmediaassignment.team3.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

    private final HashtagMapper hashtagMapper;
    private final HashtagRepository hashtagRepository;

    // Checks whether a given hashtag exists.
    public Boolean getHashtag(String label) {
        Hashtag hashtag = hashtagRepository.findByLabel(label);

        return hashtag != null;
    }
}
