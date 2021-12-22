package com.socialmediaassignment.team3.dtos;

import com.socialmediaassignment.team3.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetResponseDto {
    private Long id;
    private User author;
}
