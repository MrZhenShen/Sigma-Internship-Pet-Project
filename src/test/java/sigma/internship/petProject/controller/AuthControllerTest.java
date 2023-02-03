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
import org.springframework.test.web.servlet.MvcResult;
import sigma.internship.petProject.dto.LogInUserDto;
import sigma.internship.petProject.dto.SignUpUserDto;
import sigma.internship.petProject.dto.TokenDto;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/scripts/create/create-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/delete/clear-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private static final String GLOBAL_MAPPING = "/api/auth";

    @Nested
    class Register {

        private static final String REGISTER_MAPPING = GLOBAL_MAPPING + "/register";

        private static SignUpUserDto user;

        @Test
        void Should_ReturnBadRequest_When_AllEmptyFields() throws Exception {
            user = new SignUpUserDto("", "", "");

            mockMvc.perform(post(REGISTER_MAPPING)
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
            ;
        }

        @Test
        void Should_ReturnBadRequest_When_TwoEmptyField() throws Exception {
            user = new SignUpUserDto("", "joe@mail.com", "");

            mockMvc.perform(post(REGISTER_MAPPING)
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
            ;
        }

        @Test
        void Should_ReturnBadRequest_When_OneEmptyField() throws Exception {
            user = new SignUpUserDto("", "joe@mail.com", "joe");

            mockMvc.perform(post(REGISTER_MAPPING)
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
            ;
        }

        @Test
        void Should_ReturnBadRequest_When_InvalidEmail() throws Exception {
            user = new SignUpUserDto("joe", "joe.email.com", "joe");

            mockMvc.perform(post(REGISTER_MAPPING)
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
            ;
        }

        @Test
        void Should_ReturnUserDto_When_AuthUserDtoIsValid() throws Exception {
            user = new SignUpUserDto("Joe", "joe@email.com", "joe");

            mockMvc.perform(post(REGISTER_MAPPING)
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.accessToken", notNullValue()))
                    .andExpect(jsonPath("$.refreshToken", notNullValue()))
                    .andExpect(jsonPath("$.userId", notNullValue()))
            ;
        }

        @Test
        void Should_ThrowConflict_When_AuthUserDtoExists() throws Exception {
            user = new SignUpUserDto("userTest", "joe@email.com", "joe");

            mockMvc.perform(post(REGISTER_MAPPING)
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isConflict())
            ;
        }
    }

    @Nested
    class LogIn {
        String userUsername = "userTest";
        String userPassword = "user";

        String adminUsername = "adminTest";
        String adminPassword = "admin";

        private LogInUserDto logInUserDto;

        private static final String LOGIN_MAPPING = GLOBAL_MAPPING + "/login";

        @Test
        void Should_ReturnUserFullDto_When_UserIsValid() throws Exception {
            logInUserDto = new LogInUserDto(userUsername, userPassword);

            mockMvc.perform(post(LOGIN_MAPPING)
                            .content(objectMapper.writeValueAsString(logInUserDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken", notNullValue()))
                    .andExpect(jsonPath("$.refreshToken", notNullValue()))
                    .andExpect(jsonPath("$.userId", notNullValue()))
            ;
        }

        @Test
        void Should_ReturnUserFullDto_When_AdminIsValid() throws Exception {
            logInUserDto = new LogInUserDto(adminUsername, adminPassword);

            mockMvc.perform(post(LOGIN_MAPPING)
                            .content(objectMapper.writeValueAsString(logInUserDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken", notNullValue()))
                    .andExpect(jsonPath("$.refreshToken", notNullValue()))
                    .andExpect(jsonPath("$.userId", notNullValue()))
            ;
        }
    }

    @Nested
    class Token {
        String userUsername = "userTest";
        String userPassword = "user";

        private final LogInUserDto logInUserDto = new LogInUserDto(userUsername, userPassword);

        private static final String LOGIN_MAPPING = GLOBAL_MAPPING + "/login";
        private static final String TOKEN_MAPPING = GLOBAL_MAPPING + "/token";

        @Test
        void Should_ReturnNewTokenDto_When_RefreshTokenIsValid() throws Exception {
            MvcResult mvcResult = mockMvc
                    .perform(post(LOGIN_MAPPING)
                            .content(objectMapper.writeValueAsString(logInUserDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDto tokenDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TokenDto.class);

            mockMvc.perform(post(TOKEN_MAPPING)
                            .content(objectMapper.writeValueAsString(tokenDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken", notNullValue()))
                    .andExpect(jsonPath("$.refreshToken", notNullValue()))
                    .andExpect(jsonPath("$.userId", notNullValue()))
            ;
        }

        @Test
        void Should_ReturnNewTokenDto_When_AccessTokenPassedInstead() throws Exception {
            MvcResult mvcResult = mockMvc
                    .perform(post(LOGIN_MAPPING)
                            .content(objectMapper.writeValueAsString(logInUserDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn();
            TokenDto tokenDto = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TokenDto.class);

            mockMvc.perform(post(TOKEN_MAPPING)
                            .content(objectMapper.writeValueAsString(tokenDto))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken", notNullValue()))
                    .andExpect(jsonPath("$.refreshToken", notNullValue()))
                    .andExpect(jsonPath("$.userId", notNullValue()))
            ;
        }
    }
}
