package com.bsmm.login.service.mapper;

import com.bsmm.login.models.User;
import com.bsmm.login.service.dto.UserDTO;
import com.bsmm.login.service.dto.UserSignup;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(imports = {UUID.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //@Mapping(target = "id", expression = "java(UUID.randomUUID().toString())")
    User toEntity(UserSignup dto);

    //@Mapping(target = "id", source = "id")
    UserDTO toDto(User entity);
}
