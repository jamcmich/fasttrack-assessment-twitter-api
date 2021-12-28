package com.socialmediaassignment.team3.mappers;

import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TweetMapper {
    @Mapping(source = "author.credential", target = "author.username")
    TweetResponseDto entityToDto(Tweet entity);

    List<TweetResponseDto> entitiesToDtos (List<Tweet> entities);

    static String credentialToUsername(Credential credential) {
        return credential.getUsername();
    }
}
