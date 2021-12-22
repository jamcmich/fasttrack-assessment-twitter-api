package com.socialmediaassignment.team3.dtos;

import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private Timestamp joined;
    private Boolean deleted;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<Tweet> tweets;
    private List<Tweet> likedTweets;
    private List<User> followers;
    private List<User> following;
    private List<Tweet> mentions;
}
