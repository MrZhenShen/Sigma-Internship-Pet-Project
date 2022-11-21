package sigma.internship.petProject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import sigma.internship.petProject.dto.AuthUserDto;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/scripts/create-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/clear-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Nested
    public class Registration {
        @Test
        void Should_ReturnBadRequest_When_AuthUserDtoHasAllEmptyFields() throws Exception {
            AuthUserDto user = new AuthUserDto(0, "", "", "");

            mockMvc.perform(post("/user")
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

        }

        @Test
        void Should_ReturnBadRequest_When_AuthUserDtoHasTwoEmptyField() throws Exception {
            AuthUserDto user = new AuthUserDto(0, "Joe", "", "");

            mockMvc.perform(post("/user")
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

        }

        @Test
        void Should_ReturnBadRequest_When_AuthUserDtoHasOneEmptyField() throws Exception {
            AuthUserDto user = new AuthUserDto(0, "Joe", "joe@email.com", "");

            mockMvc.perform(post("/user")
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

        }

        @Test
        void Should_ReturnBadRequest_When_AuthUserDtoHasInvalidEmail() throws Exception {
            AuthUserDto user = new AuthUserDto(0, "Joe", "joe.email.com", "joe");

            mockMvc.perform(post("/user")
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

        }

        @Test
        void Should_ReturnUserDto_When_AuthUserDtoIsValid() throws Exception {
            AuthUserDto user = new AuthUserDto(0, "Joe", "joe@email.com", "joe");

            mockMvc.perform(post("/user")
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").value(user.username()))
                    .andExpect(jsonPath("$.email").value(user.email()))
                    .andExpect(jsonPath("$.id", notNullValue()));

        }
    }

    @Nested
    public class AuthenticatedUser {
        String userUsername = "userTest";
        String userPassword = "user";
        String userRole = "USER";

        String adminUsername = "adminTest";
        String adminPassword = "admin";
        String adminRole = "ADMIN";

        @Test
        void Should_ReturnUserFullDto_When_UserIsValid() throws Exception {
            mockMvc.perform(get("/user/login")
                            .with(httpBasic(userUsername, userPassword)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value(userUsername))
                    .andExpect(jsonPath("$.role").value(userRole))
                    .andExpect(jsonPath("$.id", notNullValue()))
            ;
        }

        @Test
        void Should_ReturnUserFullDto_When_AdminIsValid() throws Exception {
            mockMvc.perform(get("/user/login")
                            .with(httpBasic(adminUsername, adminPassword)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value(adminUsername))
                    .andExpect(jsonPath("$.role").value(adminRole))
                    .andExpect(jsonPath("$.id", notNullValue()));

        }
    }
}
