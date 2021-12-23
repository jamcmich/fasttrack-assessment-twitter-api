package com.socialmediaassignment.team3.dtos;

import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import com.socialmediaassignment.team3.entities.embeddable.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@Data
public class UserResponseDto {
    private Long id;
    private Credential credential;
    private Profile profile;
    private Date joined;
    private boolean deleted;
    private Set<Tweet> tweets;
    private Set<Tweet> likedTweets;
    private Set<Tweet> mentions;
    private Set<User> following;
    private Set<User> followers;
}
