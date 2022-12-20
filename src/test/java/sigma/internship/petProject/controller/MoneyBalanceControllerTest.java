package sigma.internship.petProject.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MoneyBalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @WithMockUser(authorities = "USER")
    public class Deposit {

        @Test
        void Should_Fail_When_ReturnIsNull() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=10.0"))
                    .andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$").exists());
        }

        @Test
        void Should_Fail_When_IsNoRequestParam() throws Exception {
            mockMvc.perform(post("/money-balance/deposit"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    public class Authorities {

        @Test
        @WithAnonymousUser
        void Should_Fail_When_UserIsNotAuthorized() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=10.0"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(authorities = "ADMIN")
        void Should_Fail_When_UserIsAdmin() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=10.0"))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(authorities = "USER")
        void Should_Success_When_UserHasDefaultUserRole() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=10.0"))
                    .andDo(print())
                    .andExpect(status().isAccepted());
        }
    }
}
