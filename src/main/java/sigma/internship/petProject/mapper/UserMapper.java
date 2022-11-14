package sigma.internship.petProject.mapper;

import org.mapstruct.Mapper;
import sigma.internship.petProject.dto.AuthUserDto;
import sigma.internship.petProject.dto.UserDto;
import sigma.internship.petProject.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto user);

    User toUser(AuthUserDto user);
}
