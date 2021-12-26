package com.socialmediaassignment.team3.controllers;

import com.socialmediaassignment.team3.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {
    private final ValidateService validateService;

    // Checks whether a given username exists.
    @GetMapping("/username/exists/@{username}")
    public boolean existUsername(@PathVariable String username) {
        return validateService.existUsername(username);
    }

    // Checks whether a given username exists.
    @GetMapping("/username/available/@{username}")
    public boolean availableUsername(@PathVariable String username) {
        return !validateService.existUsername(username);
    }

    // Checks whether a given hashtag exists.
    @GetMapping("/tag/exists/{label}")
    public Boolean existHashtag(@PathVariable String label) {
        return validateService.existHashtag(label);
    }
}
