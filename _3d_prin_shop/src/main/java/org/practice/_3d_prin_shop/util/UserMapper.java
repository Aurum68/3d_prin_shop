package org.practice._3d_prin_shop.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.practice._3d_prin_shop.dto.UserDto;
import org.practice._3d_prin_shop.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(target = "password", ignore = true)
    UserDto userToUserDto(User user);
    User userDtoToUser(UserDto userDto);
    List<UserDto> toDtoList(List<User> users);
}
