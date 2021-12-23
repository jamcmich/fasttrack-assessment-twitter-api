package com.socialmediaassignment.team3.dtos;

import com.socialmediaassignment.team3.entities.embeddable.Credential;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TweetRequestDto {
    private String content;
    private Credential credentials;
}
