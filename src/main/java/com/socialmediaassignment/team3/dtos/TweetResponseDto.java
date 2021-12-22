package com.socialmediaassignment.team3.dtos;

import com.socialmediaassignment.team3.entities.Hashtag;
import com.socialmediaassignment.team3.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class TweetResponseDto {
    private Long id;
    private User author;
    private List<User> likes;
    private List<Hashtag> hashtags;
    private List<User> usersMentioned;
}
