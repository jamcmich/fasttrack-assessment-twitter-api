package com.socialmediaassignment.team3.dtos;

import com.socialmediaassignment.team3.entities.Tweet;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ContextResponseDto {
    private Tweet target;
    private List<Tweet> before;
    private List<Tweet> after;
}
