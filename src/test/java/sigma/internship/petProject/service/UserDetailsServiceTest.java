package sigma.internship.petProject.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class UserDetailsServiceTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void Should_Fail_When_DatabaseIsEmpty() {
        String username = "name";
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));

        String expectedMessage = "User details not found for the user " + username;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

}
