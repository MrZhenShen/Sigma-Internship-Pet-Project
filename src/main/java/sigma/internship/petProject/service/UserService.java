package sigma.internship.petProject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sigma.internship.petProject.dto.AuthUserDto;
import sigma.internship.petProject.dto.UserDto;

public interface UserService {

    UserDto register(AuthUserDto user);

    UserDto getUserByUsername(String username);

    Page<UserDto> getAllUsers(Pageable pageable);
}
