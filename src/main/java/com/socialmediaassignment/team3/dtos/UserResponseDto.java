package com.socialmediaassignment.team3.dtos;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
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

    private String username;
    private Profile profile;
    private Date joined;
}
