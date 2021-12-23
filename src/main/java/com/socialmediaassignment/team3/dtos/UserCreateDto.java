package com.socialmediaassignment.team3.dtos;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import com.socialmediaassignment.team3.entities.embeddable.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@Data
public class UserCreateDto {
    private Credential credential;
    private Profile profile;
}
