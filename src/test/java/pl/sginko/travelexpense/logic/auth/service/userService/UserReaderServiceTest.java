package pl.sginko.travelexpense.logic.auth.service.userService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.sginko.travelexpense.logic.auth.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.auth.exception.UserException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class UserReaderServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserReaderService userReaderService;

    @Test
    void should_find_user_by_email() {
        // GIVEN
        String email = "test@example.com";
        String name = "name";
        String surname = "surname";
        String password = "password123";
        UserRequestDto userRequestDto = new UserRequestDto(email, name, surname, password);
        userService.addUser(userRequestDto);

        // WHEN
        UserEntity userByEmail = userReaderService.findUserByEmail(email);

        // THEN
        assertThat(userByEmail).isNotNull();
        assertThat(userByEmail.getEmail()).isEqualTo(email);
        assertThat(userByEmail.getName()).isEqualTo(name);
        assertThat(userByEmail.getSurname()).isEqualTo(surname);
    }

    @Test
    void should_throw_user_exception_if_user_not_found() {
        // GIVEN
        String email = "test2@mail.com";

        // WHEN
        Executable executable = () -> userReaderService.findUserByEmail(email);

        // THEN
        UserException exception = assertThrows(UserException.class, executable);
        assertThat(exception.getMessage()).contains("Can not find user with this email: " + email);
    }
}
