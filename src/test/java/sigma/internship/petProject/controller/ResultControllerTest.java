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
@Sql(scripts = "/scripts/create/create-result.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/delete/clear-result.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String REQUEST_MAPPING = "/result";

    @Nested
    public class FindAll {
        @Test
        @WithMockUser(authorities = "ADMIN")
        void Should_ReturnResults_When_isAdmin() throws Exception {
            mockMvc.perform(get(REQUEST_MAPPING))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.size()").value(2))
            ;
        }

        @Test
        @WithMockUser(authorities = "USER")
        void Should_Fail_When_isUser() throws Exception {
            mockMvc.perform(get(REQUEST_MAPPING))
                    .andDo(print())
                    .andExpect(status().isForbidden())
            ;
        }

        @Test
        @WithAnonymousUser
        void Should_Fail_When_isAnonymousUser() throws Exception {
            mockMvc.perform(get(REQUEST_MAPPING))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
            ;
        }
    }

}
