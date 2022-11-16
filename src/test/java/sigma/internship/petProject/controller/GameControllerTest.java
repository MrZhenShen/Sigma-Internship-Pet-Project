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
@Sql(scripts = "/scripts/create-game.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/clear-game.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    public class FindAll {
        @Test
        @WithAnonymousUser
        void successful() throws Exception {
            mockMvc.perform(get("/game"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
            ;
        }
    }

    @Nested
    public class FindAllDetailed {
        @Test
        @WithMockUser(authorities = "ADMIN")
        void successful() throws Exception {
            mockMvc.perform(get("/game/detailed"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1))
            ;
        }

        @Test
        @WithMockUser(authorities = "USER")
        void failForUser() throws Exception {
            mockMvc.perform(get("/game/detailed"))
                    .andDo(print())
                    .andExpect(status().isForbidden())
            ;
        }

        @Test
        @WithAnonymousUser
        void failForAnonymousUser() throws Exception {
            mockMvc.perform(get("/game/detailed"))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
            ;
        }
    }

    @Nested
    public class FindById {
        @Test
        @WithAnonymousUser
        void successful() throws Exception {
            int expectedId = 1;
            mockMvc.perform(get("/game/"+ expectedId))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(expectedId))
            ;
        }

        @Test
        @WithAnonymousUser
        void failNotFound() throws Exception {
            mockMvc.perform(get("/game/10"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
            ;
        }
    }
}