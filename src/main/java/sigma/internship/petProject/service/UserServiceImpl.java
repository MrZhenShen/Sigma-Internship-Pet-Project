package sigma.internship.petProject.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.dto.UserDto;
import sigma.internship.petProject.entity.MoneyBalance;
import sigma.internship.petProject.entity.RoleType;
import sigma.internship.petProject.entity.User;
import sigma.internship.petProject.exception.WebException;
import sigma.internship.petProject.mapper.UserMapper;
import sigma.internship.petProject.repository.MoneyBalanceRepository;
import sigma.internship.petProject.repository.RoleRepository;
import sigma.internship.petProject.repository.UserRepository;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserDetailsManager, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MoneyBalanceRepository moneyBalanceRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto findByUsername(String username) {
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

    @Override
    public void createUser(UserDetails userDetails) {
        log.debug("Starting creating user: {}", userDetails.getUsername());

        if (!(userDetails instanceof User user)) {
            log.error("UserDetails: {} cannot be casted to User", userDetails);
            throw new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with user casting");
        }

        log.debug("Starting validation if user with \"{}\" username exists", user.getUsername());
        if (userRepository.existsByUsername(user.getUsername())) {
            log.error("{} already exists", user.getUsername());
            throw new WebException(HttpStatus.CONFLICT, "User with such username already exists");
        }

        log.debug("Starting assigning {} to {}", RoleType.ROLE_USER, user.getUsername());
        user.setRole(roleRepository
                .findByType(RoleType.ROLE_USER)
                .orElseThrow(() -> {
                    log.error("Role {} is not found", RoleType.ROLE_USER);
                    return new WebException(HttpStatus.INTERNAL_SERVER_ERROR, "Issue with setting role for user");
                }));

        log.debug("Starting encoding password");
        user.setPassword(passwordEncoder.encode(userDetails.getPassword()));

        log.debug("Starting saving user: {}", user);
        User newUser = userRepository.save(user);

        moneyBalanceRepository.save(MoneyBalance
                .builder()
                .player(newUser)
                .amount(BigDecimal.valueOf(0))
                .build());
        log.info("Money balance for new user was successfully created");
    }

    @Override
    public void updateUser(UserDetails user) {
        // TODO
    }

    @Override
    public void deleteUser(String username) {
        // TODO
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new WebException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        MessageFormat.format("username {0} not found", username)
                ));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>(List.of(new SimpleGrantedAuthority(user.getRole().getType().name())));

        user.setAuthorities(authorities);

        return user;
    }
}
