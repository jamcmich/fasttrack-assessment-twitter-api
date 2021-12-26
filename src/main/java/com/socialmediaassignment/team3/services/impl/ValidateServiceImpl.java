package com.socialmediaassignment.team3.services.impl;

import com.socialmediaassignment.team3.entities.Hashtag;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.repositories.HashtagRepository;
import com.socialmediaassignment.team3.repositories.UserRepository;
import com.socialmediaassignment.team3.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;

    @Override
    public boolean existHashtag(String label) {
        Optional<Hashtag> hashtagOptional = hashtagRepository.findByLabel(label);
        return hashtagOptional.isPresent();
    }

    @Override
    public boolean existUsername(String username) {
        Optional<User> userOptional = userRepository.findByCredentialUsername(username);
        return userOptional.isPresent();
    }
}
