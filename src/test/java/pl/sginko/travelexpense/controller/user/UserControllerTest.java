package pl.sginko.travelexpense.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.sginko.travelexpense.domain.user.dto.UserRequestDto;
import pl.sginko.travelexpense.domain.user.entity.Roles;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;
import pl.sginko.travelexpense.domain.user.mapper.UserMapper;
import pl.sginko.travelexpense.domain.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @MockBean
    private JavaMailSender javaMailSender;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    // POST /api/v1/users/new-user
    // HAPPY PATH
    @Test
    public void should_create_user_successfully() throws Exception {
        // GIVEN
        UserRequestDto userRequest = new UserRequestDto("user@test.com", "name", "surname", "password");

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/users/new-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated());

        Assertions.assertTrue(userRepository.findByEmail("user@test.com").isPresent());
    }

    // POST /api/v1/users/new-user
    // UNHAPPY PATH
    @Test
    public void should_fail_to_create_user_with_existing_email() throws Exception {
        // GIVEN
        UserRequestDto userRequestDto = new UserRequestDto("user@test.com", "name", "surname", "password");
        userRepository.save(userMapper.toEntity(userRequestDto));

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/users/new-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message").value("User with this email already exists"));
    }


    // GET /api/v1/users/all-users
    // HAPPY PATH
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void should_get_all_users_successfully() throws Exception {
        // GIVEN
        UserRequestDto user1 = new UserRequestDto("user1@test.com", "name1", "surname1", "password");
        userRepository.save(userMapper.toEntity(user1));

        UserRequestDto user2 = new UserRequestDto("user2@test.com", "name2", "surname2", "password");
        userRepository.save(userMapper.toEntity(user2));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/users/all-users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].email", containsInAnyOrder("user1@test.com", "user2@test.com")));
    }

    // GET /api/v1/users/all-users
    // UNHAPPY PATH
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void should_return_forbidden_when_non_admin_tries_to_get_all_users() throws Exception {
        // GIVEN
        UserRequestDto user1 = new UserRequestDto("user1@test.com", "name1", "surname1", "password");
        userRepository.save(userMapper.toEntity(user1));

        UserRequestDto user2 = new UserRequestDto("user2@test.com", "name2", "surname2", "password");
        userRepository.save(userMapper.toEntity(user2));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/users/all-users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // PATCH /api/v1/users/{email}/change-role-to-accountant
    // HAPPY PATH
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void should_change_role_user_to_accountant_successfully() throws Exception {
        // GIVEN
        String email = "user@test.com";

        UserRequestDto user = new UserRequestDto("user@test.com", "name", "surname", "password");
        userRepository.save(userMapper.toEntity(user));

        // WHEN & THEN
        mockMvc.perform(patch("/api/v1/users/{email}/change-role-to-accountant", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        UserEntity updatedUser = userRepository.findByEmail(email).orElseThrow();
        assertThat(updatedUser.getRoles()).isEqualTo(Roles.ROLE_ACCOUNTANT);
    }

    // PATCH /api/v1/users/{email}/change-role-to-accountant
    // UNHAPPY PATH
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void should_return_forbidden_when_non_admin_tries_to_change_role_to_accountant() throws Exception {
        // GIVEN
        String email = "user@test.com";

        UserRequestDto user = new UserRequestDto("user@test.com", "name", "surname", "password");
        userRepository.save(userMapper.toEntity(user));

        // WHEN & THEN
        mockMvc.perform(patch("/api/v1/users/{email}/change-role-to-accountant", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // PATCH /api/v1/users/{email}/change-role-to-accountant
    // UNHAPPY PATH
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void should_fail_to_change_role_to_accountant_for_nonexistent_user() throws Exception {
        // GIVEN
        String email = "nonexistent@test.com";

        // WHEN & THEN
        mockMvc.perform(patch("/api/v1/users/{email}/change-role-to-accountant", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Can not find user with this email: " + email));
    }


    // PATCH /api/v1/users/{email}/change-role-to-manager
    // HAPPY PATH
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void should_change_role_user_to_manager_successfully() throws Exception {
        // GIVEN
        String email = "user@test.com";

        UserRequestDto user = new UserRequestDto("user@test.com", "name", "surname", "password");
        userRepository.save(userMapper.toEntity(user));

        // WHEN & THEN
        mockMvc.perform(patch("/api/v1/users/{email}/change-role-to-manager", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        UserEntity updatedUser = userRepository.findByEmail(email).orElseThrow();
        assertThat(updatedUser.getRoles()).isEqualTo(Roles.ROLE_MANAGER);
    }

    // PATCH /api/v1/users/{email}/change-role-to-accountant
    // UNHAPPY PATH
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void should_return_forbidden_when_non_admin_tries_to_change_role_to_manager() throws Exception {
        // GIVEN
        String email = "user@test.com";

        UserRequestDto user = new UserRequestDto("user@test.com", "name", "surname", "password");
        userRepository.save(userMapper.toEntity(user));

        // WHEN & THEN
        mockMvc.perform(patch("/api/v1/users/{email}/change-role-to-manager", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // PATCH /api/v1/users/{email}/change-role-to-manager
    // UNHAPPY PATH
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void should_fail_to_change_role_to_manager_for_nonexistent_user() throws Exception {
        // GIVEN
        String email = "nonexistent@test.com";

        // WHEN & THEN
        mockMvc.perform(patch("/api/v1/users/{email}/change-role-to-manager", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Can not find user with this email: " + email));
    }
}
