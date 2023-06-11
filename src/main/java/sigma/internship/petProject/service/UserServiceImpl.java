package sigma.internship.petProject.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.dto.AuthUserDto;
import sigma.internship.petProject.dto.UserDto;
import sigma.internship.petProject.entity.MoneyBalance;
import sigma.internship.petProject.entity.Role;
import sigma.internship.petProject.entity.User;
import sigma.internship.petProject.exception.WebException;
import sigma.internship.petProject.mapper.UserMapper;
import sigma.internship.petProject.repository.MoneyBalanceRepository;
import sigma.internship.petProject.repository.UserRepository;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MoneyBalanceRepository moneyBalanceRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(AuthUserDto user) {
        log.info("Starting creating new user");

        checkIfUserExist(user.username());

        User userEntity = userMapper.toUser(user);
        userEntity.setRole(Role.USER);

        encodePassword(userEntity, user);

        User newUser = userRepository.save(userEntity);
        log.info("New user was successfully created");

        moneyBalanceRepository.save(MoneyBalance
                .builder()
                .player(newUser)
                .amount(BigDecimal.valueOf(0))
                .build());
        log.info("Money balance for new user was successfully created");

        return userMapper.toDto(newUser);
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
    public Page<UserDto> getAllUsers(Pageable pageable) {
        log.info("Staring retrieving all user data");

        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    public void checkIfUserExist(String username) {
        log.info("Starting checking if user with \"{}\" username exists", username);

        if (userRepository.findByUsername(username).isPresent()) {
            log.error("{} user already exists", username);
            throw new WebException(HttpStatus.CONFLICT, "User already exists");
        }
    }

    private void encodePassword(User userEntity, AuthUserDto user) {
        log.info("Staring encoding password");

        userEntity.setPassword(passwordEncoder.encode(user.password()));
    }
}
