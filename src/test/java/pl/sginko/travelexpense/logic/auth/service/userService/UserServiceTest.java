package pl.sginko.travelexpense.logic.auth.service.userService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.sginko.travelexpense.logic.auth.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.auth.dto.UserResponseDto;
import pl.sginko.travelexpense.logic.auth.entity.Roles;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.auth.exception.UserException;
import pl.sginko.travelexpense.logic.auth.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
    private final String EMAIL = "test@example.com";
    private final String NAME = "Name";
    private final String SURNAME = "Surname";
    private final String PASSWORD = "password123";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void should_add_user() {
        // GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(EMAIL, NAME, SURNAME, PASSWORD);

        // WHEN
        userService.addUser(userRequestDto);

        // THEN
        assertThat(userRepository.findAll()).isNotEmpty();
    }

    @Test
    void should_throw_exception_when_user_already_exists() {
        // GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(EMAIL, NAME, SURNAME, PASSWORD);
        userService.addUser(userRequestDto);

        // WHEN
        Executable e = () -> userService.addUser(userRequestDto);

        // THEN
        UserException exc = assertThrows(UserException.class, e);
        assertThat(exc.getMessage().contains("User with this email already exists")).isTrue();
    }

    @Test
    void should_find_all_users() {
        // GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(EMAIL, NAME, SURNAME, PASSWORD);
        userService.addUser(userRequestDto);

        // WHEN
        List<UserResponseDto> allUsers = userService.findAllUser();

        // THEN
        assertThat(allUsers).isNotEmpty().hasSize(1);
    }

    @Test
    void should_change_user_role_to_accountant() {
        // GIVEN
        UserRequestDto userRequestDto = new UserRequestDto(EMAIL, NAME, SURNAME, PASSWORD);
        userService.addUser(userRequestDto);

        // WHEN
        userService.changeUserRoleToAccountant(EMAIL);

        // THEN
        UserEntity user = userRepository.findByEmail(EMAIL).orElseThrow();
        assertThat(user.getRoles()).isEqualTo(Roles.ROLE_ACCOUNTANT);
    }
}
