package pl.sginko.travelexpense.domain.user.service.userService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;
import pl.sginko.travelexpense.domain.user.exception.UserException;
import pl.sginko.travelexpense.domain.user.repository.UserReaderRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserReaderServiceTest {
    @Mock
    private UserReaderRepository userReaderRepository;

    @InjectMocks
    private UserReaderServiceImpl userReaderService;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity("user@test.com", "name", "surname", "password");
    }

    @Test
    void should_find_user_by_email_successfully() {
        // GIVEN
        when(userReaderRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        // WHEN
        UserEntity result = userReaderService.findUserByEmail(userEntity.getEmail());

        // THEN
        assertThat(result).isEqualTo(userEntity);
        verify(userReaderRepository, times(1)).findByEmail(userEntity.getEmail());
    }

    @Test
    void should_throw_exception_when_user_not_found_by_email() {
        // GIVEN
        String email = "nonexistent@test.com";
        when(userReaderRepository.findByEmail(email)).thenReturn(Optional.empty());

        // WHEN
        Executable executable = () -> userReaderService.findUserByEmail(email);

        // THEN
        assertThrows(UserException.class, executable, "Can not find user with this email: " + email);
        verify(userReaderRepository, times(1)).findByEmail(email);
    }
}
