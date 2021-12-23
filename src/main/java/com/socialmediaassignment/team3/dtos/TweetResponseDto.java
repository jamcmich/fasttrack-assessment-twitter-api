package com.socialmediaassignment.team3.dtos;

import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class TweetResponseDto {
    private Long id;
    private User author;
    private String content;
    private Date posted;
    private Tweet inReplyTo;
    private Tweet repostOf;
}
