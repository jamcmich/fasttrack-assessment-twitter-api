package com.socialmediaassignment.team3.mappers;

import com.socialmediaassignment.team3.dtos.HashtagResponseDto;
import com.socialmediaassignment.team3.entities.Hashtag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HashtagMapper {
    HashtagResponseDto entityToDto(Hashtag entity);

    List<HashtagResponseDto> entitiesToDtos (List<Hashtag> entities);
}
