package pl.sginko.travelexpense.logic.auth.service.userService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sginko.travelexpense.logic.user.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.user.dto.UserResponseDto;
import pl.sginko.travelexpense.logic.user.entity.Roles;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.mapper.UserMapper;
import pl.sginko.travelexpense.logic.user.repository.UserRepository;
import pl.sginko.travelexpense.logic.user.service.userService.UserServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDto userRequestDto;
    private UserEntity userEntity;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto("test@example.com", "John", "Doe", "password123");
        userEntity = new UserEntity("test@example.com", "John", "Doe", "encodedPassword");
        userResponseDto = new UserResponseDto("test@example.com", "John", "Doe", Roles.ROLE_USER);
    }

    @Test
    void should_add_user_successfully() {
        // GIVEN
        when(userRepository.findByEmail(userRequestDto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toEntity(userRequestDto)).thenReturn(userEntity);

        // WHEN
        userService.addUser(userRequestDto);

        // THEN
        verify(userRepository, times(1)).findByEmail(userRequestDto.getEmail());
        verify(userMapper, times(1)).toEntity(userRequestDto);
        verify(userRepository, times(1)).save(userEntity);
    }

//    @Test
//    void should_throw_exception_when_user_already_exists() {
//        // GIVEN
//        when(userRepository.findByEmail(userRequestDto.getEmail())).thenReturn(Optional.of(userEntity));
//
//        // WHEN
//        Executable executable = () -> userService.addUser(userRequestDto);
//
//        // THEN
//        assertThatThrownBy(executable)
//                .isInstanceOf(UserException.class)
//                .hasMessage("User with this email already exists");
//        verify(userRepository, times(1)).findByEmail(userRequestDto.getEmail());
//        verify(userRepository, never()).save(any(UserEntity.class));
//    }

    @Test
    void should_find_all_users() {
        // GIVEN
        when(userRepository.findAll()).thenReturn(Arrays.asList(userEntity));
        when(userMapper.fromEntity(userEntity)).thenReturn(userResponseDto);

        // WHEN
        List<UserResponseDto> users = userService.findAllUser();

        // THEN
        assertThat(users).hasSize(1).contains(userResponseDto);
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).fromEntity(userEntity);
    }

    @Test
    void should_change_user_role_to_accountant() {
        // GIVEN
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        // WHEN
        userService.changeUserRoleToAccountant(userEntity.getEmail());

        // THEN
        assertThat(userEntity.getRoles()).isEqualTo(Roles.ROLE_ACCOUNTANT);
        verify(userRepository, times(1)).findByEmail(userEntity.getEmail());
    }

    @Test
    void should_change_user_role_to_manager() {
        // GIVEN
        when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));

        // WHEN
        userService.changeUserRoleToManager(userEntity.getEmail());

        // THEN
        assertThat(userEntity.getRoles()).isEqualTo(Roles.ROLE_MANAGER);
        verify(userRepository, times(1)).findByEmail(userEntity.getEmail());
    }

    @Test
    void should_save_oauth2_user_if_not_exists() {
        // GIVEN
        String email = "oauth@example.com";
        String name = "OAuth";
        String surname = "User";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        UserEntity oauthUser = new UserEntity(email, name, surname, "");
        when(userMapper.toEntityFromOAuth2(email, name, surname)).thenReturn(oauthUser);

        // WHEN
        userService.saveOAuth2User(email, name, surname);

        // THEN
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, times(1)).toEntityFromOAuth2(email, name, surname);
        verify(userRepository, times(1)).save(oauthUser);
    }

    @Test
    void should_not_save_oauth2_user_if_exists() {
        // GIVEN
        String email = "existing@example.com";
        String name = "Existing";
        String surname = "User";
        UserEntity existingUser = new UserEntity(email, name, surname, "password");
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // WHEN
        userService.saveOAuth2User(email, name, surname);

        // THEN
        verify(userRepository, times(1)).findByEmail(email);
        verify(userMapper, never()).toEntityFromOAuth2(anyString(), anyString(), anyString());
        verify(userRepository, never()).save(any(UserEntity.class));
    }
}
