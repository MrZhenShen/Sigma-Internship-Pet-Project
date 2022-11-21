package sigma.internship.petProject.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.dto.AuthUserDto;
import sigma.internship.petProject.dto.UserDto;
import sigma.internship.petProject.entity.Role;
import sigma.internship.petProject.entity.User;
import sigma.internship.petProject.exception.WebException;
import sigma.internship.petProject.mapper.UserMapper;
import sigma.internship.petProject.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(AuthUserDto user) {
        log.info("Starting creating new user: {}", user.username());
        if (checkIfUserExist(user.username())) {
            log.error("Such user already exists");
            throw new WebException(HttpStatus.CONFLICT, "User already exists");
        }
        User userEntity = userMapper.toUser(user);
        userEntity.setRole(Role.USER);

        encodePassword(userEntity, user);

        User newUser = userRepository.save(userEntity);
        log.info("User was successfully created: {}", newUser);

        return userMapper.toDto(newUser);
    }

    @Override
    public boolean checkIfUserExist(String username) {
        log.info("Starting checking if user with \"{}\" username exists", username);
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserDto getUserByUsername(String username) {
        log.info("Starting retrieving user with \"{}\" username", username);
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    log.error("User with \"{}\" username was not found", username);
                    throw new WebException(HttpStatus.NOT_FOUND, "User not found");
                });
    }

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Staring retrieving all user data");
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    private void encodePassword(User userEntity, AuthUserDto user) {
        log.info("Staring encoding password");
        userEntity.setPassword(passwordEncoder.encode(user.password()));
    }
}