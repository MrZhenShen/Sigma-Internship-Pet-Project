package sigma.internship.petProject.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/scripts/create/create-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/delete/clear-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MoneyBalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String REQUEST_MAPPING = "/money-balance";

    private static final String MOCK_USER_WITH_MONEY = "userWithMoney";
    private static final String MOCK_USER_WITHOUT_MONEY = "userWithoutMoney";
    private static final String MOCK_USER_PASSWORD = "user";
    private static final String MOCK_USER_ROLE = "USER";

    @Nested
    @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITHOUT_MONEY, password = MOCK_USER_PASSWORD)
    public class Deposit {

        @Test
        void Should_ReturnExistingMoneyBalance_When_RespondIsNull() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=10.0"))
                    .andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$").exists());
        }

        @Test
        void Should_ThrowBedRequest_When_IsNoRequestParam() throws Exception {
            mockMvc.perform(post("/money-balance/deposit"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void Should_ThrowPaymentRequired_When_DepositAmountIsZero() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=0"))
                    .andDo(print())
                    .andExpect(status().isPaymentRequired());
        }

        @Test
        void Should_ThrowPaymentRequired_When_DepositAmountIsLessThanZero() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=-1"))
                    .andDo(print())
                    .andExpect(status().isPaymentRequired());
        }

        @Test
        void Should_ReturnMoneyBalanceWithDepositedAmount_When_DepositAmountIsInt() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=1"))
                    .andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.amount").value("1.0"));
        }

        @Test
        void Should_ReturnMoneyBalanceWithDepositedAmount_When_DepositAmountWithOneDigitAfterFloat() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=1.5"))
                    .andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.amount").value("1.5"));
        }

        @Test
        void Should_ReturnMoneyBalanceWithDepositedAmount_When_DepositAmountWithTwoDigitsAfterFloat() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=1.55"))
                    .andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.amount").value("1.55"));
        }

        @Test
        @Sql(scripts = "/scripts/delete/clear-user.sql")
        void Should_ThrowInternalServerError_When_UserWasDeleted() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=1.55"))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
            ;
        }

        @Test
        @Sql(scripts = "/scripts/delete/clear-money-balance.sql")
        void Should_ThrowInternalServerError_When_MoneyBalanceWasDeleted() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=1.55"))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
            ;
        }
    }

    @Nested
    public class Withdraw {

        private static final String WITHDRAW_MAPPING = REQUEST_MAPPING + "/withdraw";

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        @Sql(scripts = "/scripts/delete/clear-money-balance.sql")
        void Should_ThrowInternalServerError_When_MoneyBalanceWasDeleted() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=10"))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_ReturnUpdateMoneyBalance_When_WithdrawIsValid() throws Exception {

            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=15.0"))
                    .andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$").exists());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_ThrowBedRequest_When_IsNoRequestParam() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_ThrowBedRequest_When_WithdrawAmountIsZero() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=0"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_ThrowBedRequest_When_WithdrawAmountIsLessThanZero() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=-1"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY, password = MOCK_USER_PASSWORD)
        void Should_ThrowBedRequest_When_WithdrawAmountIsOne() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=1"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY, password = MOCK_USER_PASSWORD)
        void Should_ThrowBedRequest_When_WithdrawAmountIsNine() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=9"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY, password = MOCK_USER_PASSWORD)
        void Should_ThrowBedRequest_When_WithdrawAmountIsNine_With_OneDigitAfterFloat() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=9.9"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY, password = MOCK_USER_PASSWORD)
        void Should_ThrowBadRequest_When_WithdrawAmountIsNine_With_TwoDigitsAfterFloat() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=9.99"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY, password = MOCK_USER_PASSWORD)
        void Should_ReturnAccepted_When_WithdrawAmountIsTen() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=10"))
                    .andDo(print())
                    .andExpect(status().isAccepted());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY, password = MOCK_USER_PASSWORD)
        void Should_ReturnAccepted_When_WithdrawAmountIsEleven() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=11"))
                    .andDo(print())
                    .andExpect(status().isAccepted());
        }

        @Test
        @WithMockUser(authorities = "USER", username = MOCK_USER_WITH_MONEY, password = MOCK_USER_PASSWORD)
        void Should_ReturnMoneyBalance_When_WithdrawAmountWithOneDigitAfterFloat() throws Exception {
            double withdrawAmount = 10.5;
            double expectedMoneyBalance = 100 - 10.5;

            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=" + withdrawAmount))
                    .andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.amount").value(expectedMoneyBalance));
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY, password = MOCK_USER_PASSWORD)
        void Should_ReturnMoneyBalance_When_WithdrawAmountWithTwoDigitsAfterFloat() throws Exception {
            double withdrawAmount = 10.5;
            double expectedMoneyBalance = 100 - 10.5;

            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=" + withdrawAmount))
                    .andDo(print())
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.amount").value(expectedMoneyBalance));
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITHOUT_MONEY, password = MOCK_USER_PASSWORD)
        void Should_ThrowBadRequest_When_BalanceIsNotEnough_And_WithdrawAmountIsOk() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=10"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITHOUT_MONEY, password = MOCK_USER_PASSWORD)
        void Should_ThrowBadRequest_When_BalanceIsNotEnough_And_WithdrawAmountIsBad() throws Exception {
            mockMvc.perform(post(WITHDRAW_MAPPING + "?amount=9"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    public class View {

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        @Sql(scripts = "/scripts/delete/clear-money-balance.sql")
        void Should_ThrowInternalServerError_When_MoneyBalanceWasDeleted() throws Exception {
            mockMvc.perform(get("/money-balance"))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITHOUT_MONEY)
        void Should_ReturnExistingMoneyBalance_When_RespondIsNull() throws Exception {
            mockMvc.perform(get("/money-balance"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITHOUT_MONEY)
        void Should_ReturnZeroBalance_When_TryToView() throws Exception {
            mockMvc.perform(get("/money-balance"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amount").value("0.0"));
        }
    }

    @Nested
    public class Authorities {

        @Test
        @WithAnonymousUser
        void Should_ThrowUnauthorized_When_UserIsNotAuthorized() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=10.0"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser(authorities = "ADMIN")
        void Should_ThrowForbidden_When_UserIsAdmin() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=10.0"))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITHOUT_MONEY)
        void Should_ReturnAccepted_When_UserHasDefaultUserRole() throws Exception {
            mockMvc.perform(post("/money-balance/deposit?amount=10.0"))
                    .andDo(print())
                    .andExpect(status().isAccepted());
        }
    }
}
