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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(
        scripts = {
                "/scripts/create/create-game.sql",
                "/scripts/create/create-user.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(
        scripts = {
                "/scripts/delete/clear-game-session.sql",
                "/scripts/delete/clear-user.sql",
                "/scripts/delete/clear-game.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GameSessionControllerTest {

    @Autowired
    MockMvc mockMvc;

    private static final String GLOBAL_MAPPING = "/api/game-session";

    private static final String MOCK_USER_WITH_MONEY = "userWithMoney";
    private static final String MOCK_USER_WITHOUT_MONEY = "userWithoutMoney";
    private static final String MOCK_USER_ROLE = "USER";

    @Nested
    class CreateGameSession {

        @Test
        @WithMockUser(authorities = "ADMIN")
        void Should_ThrowForbidden_When_isAdmin() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING).param("game", "1"))
                    .andDo(print())
                    .andExpect(status().isForbidden())
            ;
        }

        @Test
        @WithAnonymousUser
        void Should_ThrowUnauthorized_When_isAnonymousUser() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING).param("game", "1"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_ThrowBadRequest_When_NoParamIsProvided() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_ThrowBadRequest_When_NotExistingGameId() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING).param("game", "1000"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_ThrowBadRequest_When_InvalidGameId() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING).param("game", "textId"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
            ;
        }

        @Test
        @Sql(scripts = "/scripts/create/create-game.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(scripts = "/scripts/delete/clear-game.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @WithMockUser(authorities = MOCK_USER_ROLE)
        void Should_ThrowInternalServerError_When_NotExistingUser() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING).param("game", "1"))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = "userTest")
        void Should_ThrowInternalServerError_When_NotExistingMoneyBalance() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING).param("game", "1"))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITHOUT_MONEY)
        void Should_ThrowPaymentRequired_When_UserIsLackOfMoney() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING).param("game", "1"))
                    .andDo(print())
                    .andExpect(status().isPaymentRequired())
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_CreateRound_When_UserHasOneMoreUSDThanExpected() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING).param("game", "3"))
                    .andDo(print())
                    .andExpect(status().isCreated())
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_CreateRound_When_UserHasMoneyThatEqualsToGameCost() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING).param("game", "1"))
                    .andDo(print())
                    .andExpect(status().isCreated())
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_CreateRounds_When_UserHasMoneyThatEqualsToGameCost() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING).param("game", "1"))
                    .andDo(print())
                    .andExpect(status().isCreated())
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_CreateRound_When_GameIsManyActions() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING)
                            .param("game", "1")
                            .param("rounds", "2"))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.size()").value(1))
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_CreateRounds_When_GameIsOneAction() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING)
                            .param("game", "2")
                            .param("rounds", "2"))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.size()").value(2))
            ;
        }

        @Test
        @WithMockUser(authorities = MOCK_USER_ROLE, username = MOCK_USER_WITH_MONEY)
        void Should_ThrowBadRequest_When_RoundAmountParameterIsInvalidWithOneActionGame() throws Exception {
            mockMvc.perform(post(GLOBAL_MAPPING)
                            .param("game", "2")
                            .param("rounds", "0"))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
            ;
        }
    }
}
