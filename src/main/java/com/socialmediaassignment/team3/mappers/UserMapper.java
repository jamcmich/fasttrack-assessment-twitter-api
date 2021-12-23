package com.socialmediaassignment.team3.mappers;

import com.socialmediaassignment.team3.dtos.UserCreateDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto entityToDto(User entity);

    List<UserResponseDto> entitiesToDtos (List<User> entities);

    User createDtoToEntity (UserCreateDto userCreateDto);
}
