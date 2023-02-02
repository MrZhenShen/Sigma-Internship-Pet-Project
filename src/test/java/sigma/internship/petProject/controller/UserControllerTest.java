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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/scripts/create/create-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/scripts/delete/clear-user.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    private static final String GLOBAL_MAPPING = "/api/user";

    @Nested
    class ViewAll {

        private static final String VIEW_ALL_MAPPING = GLOBAL_MAPPING + "/all";

        @Test
        @WithMockUser(authorities = "ADMIN")
        void Should_ReturnAllUsers_When_isAdmin() throws Exception {
            mockMvc.perform(get(VIEW_ALL_MAPPING))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.size()").value(4))
            ;
        }

        @Test
        @WithMockUser(authorities = "USER")
        void Should_ThrowForbidden_When_isUser() throws Exception {
            mockMvc.perform(get(VIEW_ALL_MAPPING))
                    .andDo(print())
                    .andExpect(status().isForbidden())
            ;
        }

        @Test
        @WithAnonymousUser
        void Should_ThrowUnauthorized_When_isAnonymousUser() throws Exception {
            mockMvc.perform(get(VIEW_ALL_MAPPING))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
            ;
        }
    }
}
