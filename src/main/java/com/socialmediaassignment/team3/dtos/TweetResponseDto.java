package com.socialmediaassignment.team3.dtos;

import com.socialmediaassignment.team3.entities.Hashtag;
import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Data
public class TweetResponseDto {
    private Long id;
    private User author;
    private String content;
    private Date posted;
    private Tweet inReplyTo;
    private Tweet repostOf;
    private Set<User> likes;
    private Set<Hashtag> hashtags;
    private Set<User> usersMentioned;
}
