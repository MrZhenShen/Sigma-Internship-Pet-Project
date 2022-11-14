package sigma.internship.petProject.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.dto.AuthUserDto;
import sigma.internship.petProject.dto.UserDto;
import sigma.internship.petProject.entity.User;
import sigma.internship.petProject.exception.WebException;
import sigma.internship.petProject.mapper.UserMapper;
import sigma.internship.petProject.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(AuthUserDto user) {
        if (checkIfUserExist(user.username())) {
            throw new WebException(HttpStatus.CONFLICT, "User already exists");
        }
        User userEntity = userMapper.toUser(user);
        encodePassword(userEntity, user);
        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Override
    public boolean checkIfUserExist(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new WebException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    private void encodePassword(User userEntity, AuthUserDto user) {
        userEntity.setPassword(passwordEncoder.encode(user.password()));
    }
}