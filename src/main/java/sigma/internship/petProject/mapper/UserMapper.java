package sigma.internship.petProject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import sigma.internship.petProject.dto.SignUpUserDto;
import sigma.internship.petProject.dto.UserDto;
import sigma.internship.petProject.entity.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto user);

    User toEntity(SignUpUserDto user);
}
