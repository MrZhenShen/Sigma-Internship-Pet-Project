package sigma.internship.petProject.controller;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/scripts/create-twit.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/clear-twit.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class TwitControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Nested
    public class FindAll {
        @Test
        void successful() throws Exception {
            mockMvc.perform(get("/twit"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.size()").value(1))
                    .andExpect(jsonPath("$.content[0].id").value(1))
            ;
        }
    }

    @Nested
    public class FindById {
        @Test
        void successful() throws Exception {
            mockMvc.perform(get("/twit/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        void notFound() throws Exception {
            mockMvc.perform(get("/twit/2"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }
}