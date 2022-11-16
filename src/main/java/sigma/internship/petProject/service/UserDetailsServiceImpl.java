package sigma.internship.petProject.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sigma.internship.petProject.entity.User;
import sigma.internship.petProject.repository.UserRepository;

import java.util.Collections;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Started retrieving user data with \"{}\" username", username);
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    log.error("Error with retrieving user with \"{}\" username", username);
                    throw new UsernameNotFoundException("User details not found for the user " + username);
                });
        log.info("User with username: {} was successfully retrieved", username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}
