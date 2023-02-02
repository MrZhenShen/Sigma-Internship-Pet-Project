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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/scripts/create/create-game.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/delete/clear-game.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GameControllerTest {

    @Autowired
    MockMvc mockMvc;

    private static final String GLOBAL_MAPPING = "/api/game";

    @Nested
    class FindAll {
        @Test
        @WithAnonymousUser
        void Should_ReturnListOfGames_WhenRequestForAllGames() throws Exception {
            mockMvc.perform(get(GLOBAL_MAPPING))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(3))
                    .andExpect(jsonPath("$[0].id").value(1))
            ;
        }

        @Test
        @Sql(scripts = {"/scripts/delete/clear-game.sql"})
        @WithAnonymousUser
        void Should_ThrowInternalServerError_When_DatabaseIsEmpty() throws Exception {
            mockMvc.perform(get(GLOBAL_MAPPING))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
            ;
        }
    }

    @Nested
    class FindAllDetailed {

        private static final String FIND_ALL_DETAILED_MAPPING = GLOBAL_MAPPING + "/detailed";

        @Test
        @WithMockUser(authorities = "ADMIN")
        void Should_ReturnOrderedListWithThreeGames_When_ThereAreThreeGames() throws Exception {
            mockMvc.perform(get(FIND_ALL_DETAILED_MAPPING))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(3))
                    .andExpect(jsonPath("$[0].id").value(1))
            ;
        }

        @Test
        @WithMockUser(authorities = "USER")
        void Should_ThrowForbidden_When_IsUser() throws Exception {
            mockMvc.perform(get(FIND_ALL_DETAILED_MAPPING))
                    .andDo(print())
                    .andExpect(status().isForbidden())
            ;
        }

        @Test
        @WithAnonymousUser
        void Should_ThrowUnauthorized_When_IsAnonymousUser() throws Exception {
            mockMvc.perform(get(FIND_ALL_DETAILED_MAPPING))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
            ;
        }

        @Test
        @Sql(scripts = {"/scripts/delete/clear-game.sql"})
        @WithMockUser(authorities = "ADMIN")
        void Should_ThrowInternalServerError_When_DatabaseIsEmpty() throws Exception {
            mockMvc.perform(get(FIND_ALL_DETAILED_MAPPING))
                    .andDo(print())
                    .andExpect(status().isInternalServerError())
            ;
        }
    }

    @Nested
    class FindById {
        @Test
        @WithAnonymousUser
        void Should_ReturnExpectedGame_When_IdIsProvided() throws Exception {
            int expectedId = 1;
            mockMvc.perform(get(GLOBAL_MAPPING + "/" + expectedId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(expectedId))
            ;
        }

        @Test
        @WithAnonymousUser
        void Should_ThrowNotFound_When_NoGameFoundWithNotExistingId() throws Exception {
            mockMvc.perform(get(GLOBAL_MAPPING + "/10"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
            ;
        }
    }
}