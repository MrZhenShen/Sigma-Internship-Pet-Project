package sigma.internship.petProject.service;

import sigma.internship.petProject.dto.AuthUserDto;
import sigma.internship.petProject.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto register(AuthUserDto user);

    boolean checkIfUserExist(String username);

    UserDto getUserByUsername(String username);

    List<UserDto> getAllUsers();
}
