package com.socialmediaassignment.team3.mappers;

import com.socialmediaassignment.team3.dtos.TweetResponseDto;
import com.socialmediaassignment.team3.entities.Tweet;
import com.socialmediaassignment.team3.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TweetMapper {
    TweetResponseDto entityToDto(Tweet entity);

    List<TweetResponseDto> entitiesToDtos (List<Tweet> entities);
}
