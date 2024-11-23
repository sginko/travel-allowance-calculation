/*
 * Copyright 2024 Sergii Ginkota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.sginko.travelexpense.domain.user.service.userService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.domain.user.dto.UserRequestDto;
import pl.sginko.travelexpense.domain.user.dto.UserResponseDto;
import pl.sginko.travelexpense.domain.user.entity.Roles;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;
import pl.sginko.travelexpense.domain.user.exception.UserException;
import pl.sginko.travelexpense.domain.user.exception.UserNotFoundException;
import pl.sginko.travelexpense.domain.user.mapper.UserMapper;
import pl.sginko.travelexpense.domain.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceIntegrationTest {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private JavaMailSender javaMailSender;

    private UserRequestDto userRequestDto;

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        userRequestDto = new UserRequestDto("test@test.com", "name", "surname", "password");

        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin@test.com", "password", "ROLE_ADMIN"));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void should_add_user_successfully() {
        // WHEN
        userService.addUser(userRequestDto);

        // THEN
        UserEntity savedUser = userRepository.findByEmail("test@test.com").orElseThrow();
        assertThat(savedUser.getEmail()).isEqualTo("test@test.com");
        assertThat(savedUser.getRoles()).isEqualTo(Roles.ROLE_USER);
    }

    @Test
    void should_throw_exception_when_user_already_exists() {
        // GIVEN
        userRepository.save(new UserEntity("test@test.com", "name", "surname", "password"));

        // WHEN
        Executable e = () -> userService.addUser(userRequestDto);

        // THEN
        assertThrows(UserException.class, e, "Can not find user with this email: ");
    }

    @Test
    void should_change_user_role_to_accountant() {
        // GIVEN
        userRepository.save(new UserEntity("test@test.com", "name", "surname", "password"));

        // WHEN
        userService.changeUserRoleToAccountant("test@test.com");

        // THEN
        UserEntity updatedUser = userRepository.findByEmail("test@test.com").orElseThrow();
        assertThat(updatedUser.getRoles()).isEqualTo(Roles.ROLE_ACCOUNTANT);
    }

    @Test
    void should_change_user_role_to_manager() {
        // GIVEN
        userRepository.save(new UserEntity("test@test.com", "name", "surname", "password"));

        // WHEN
        userService.changeUserRoleToManager("test@test.com");

        // THEN
        UserEntity updatedUser = userRepository.findByEmail("test@test.com").orElseThrow();
        assertThat(updatedUser.getRoles()).isEqualTo(Roles.ROLE_MANAGER);
    }

    @Test
    void should_throw_exception_when_user_not_found_for_role_change() {
        // WHEN
        Executable e = () -> userService.changeUserRoleToAccountant("nonexistent@test.com");

        // THEN
        assertThrows(UserNotFoundException.class, e, "Can not find user with this email: nonexistent@test.com");
    }

    @Test
    void should_find_all_users() {
        // GIVEN
        userRepository.save(new UserEntity("test1@test.com", "name1", "surname1", "password1"));
        userRepository.save(new UserEntity("test2@test.com", "name2", "surname2", "password2"));

        // WHEN
        List<UserResponseDto> allUser = userService.findAllUser();

        // THEN
        assertThat(allUser).hasSize(2)
                .extracting(UserResponseDto::getEmail)
                .containsExactlyInAnyOrder("test1@test.com", "test2@test.com");
    }

    @Test
    void should_not_save_oauth2_user_if_exists() {
        // GIVEN
        UserEntity existingUserEntity = new UserEntity("existing@test.com", "Existing", "User", "password");
        userRepository.save(existingUserEntity);

        // WHEN
        userService.saveOAuth2User("existing@test.com", "Existing", "User");

        // THEN
        UserEntity existingUser = userRepository.findByEmail("existing@test.com").orElseThrow();
        assertThat(existingUser.getEmail()).isEqualTo("existing@test.com");
        assertThat(existingUser.getName()).isEqualTo("Existing");
        assertThat(existingUser.getSurname()).isEqualTo("User");
    }
}
