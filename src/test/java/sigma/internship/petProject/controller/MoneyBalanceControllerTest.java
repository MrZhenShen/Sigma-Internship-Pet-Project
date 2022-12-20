package sigma.internship.petProject.controller;

import org.junit.jupiter.api.Disabled;
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
//@Sql(scripts = {"/scripts/create-users.sql", "/scripts/create-money-balance.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = {"/scripts/clear-money-balance.sql", "/scripts/clear-user.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MoneyBalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @WithMockUser(authorities = "USER", username = "userTest", password = "user")
    public class Deposit {

        @Test
        @Disabled("Disable till resolving issue with hsql")
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

        @Test
        @Disabled("Disable till resolving issue with hsql")
        void Should_Fail_When_DepositAmountIsZero() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=0"))
                    .andDo(print())
                    .andExpect(status().isPaymentRequired());
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
        @Disabled("Disable till resolving issue with hsql")
        @WithMockUser(authorities = "USER")
        void Should_Success_When_UserHasDefaultUserRole() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=10.0"))
                    .andDo(print())
                    .andExpect(status().isAccepted());
        }
    }
}
