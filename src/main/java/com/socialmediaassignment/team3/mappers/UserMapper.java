package com.socialmediaassignment.team3.mappers;

import com.socialmediaassignment.team3.dtos.UserRequestDto;
import com.socialmediaassignment.team3.dtos.UserResponseDto;
import com.socialmediaassignment.team3.entities.User;
import com.socialmediaassignment.team3.entities.embeddable.Credential;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "credential", target = "username")
    UserResponseDto entityToDto(User entity);

    List<UserResponseDto> entitiesToDtos (List<User> entities);

    User createDtoToEntity (UserRequestDto userRequestDto);

    static String credentialToUsername(Credential credential) {
        return credential.getUsername();
    }
}
