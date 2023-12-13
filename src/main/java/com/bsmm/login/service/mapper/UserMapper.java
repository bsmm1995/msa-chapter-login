package com.bsmm.login.service.mapper;

import com.bsmm.login.models.Role;
import com.bsmm.login.models.User;
import com.bsmm.login.service.dto.UserDTO;
import com.bsmm.login.service.dto.UserSignup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(imports = {UUID.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    User toEntity(UserSignup dto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoles")
    UserDTO toDto(User entity);

    @Named("mapRoles")
    default Set<String> mapRoles(Set<Role> roles) {
        return roles.stream().map(role -> role.getName().name()).collect(Collectors.toSet());
    }
}
