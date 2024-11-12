package pl.sginko.travelexpense.logic.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sginko.travelexpense.logic.user.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.user.dto.UserResponseDto;
import pl.sginko.travelexpense.logic.user.entity.Roles;
import pl.sginko.travelexpense.logic.user.entity.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UserMapperTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_map_to_entity_with_encoded_password() {
        // GIVEN
        UserRequestDto requestDto = new UserRequestDto("user@example.com", "John", "Doe", "password123");
        String encodedPassword = "encodedPassword123";

        when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);

        // WHEN
        UserEntity userEntity = userMapper.toEntity(requestDto);

        // THEN
        assertThat(userEntity.getEmail()).isEqualTo("user@example.com");
        assertThat(userEntity.getName()).isEqualTo("John");
        assertThat(userEntity.getSurname()).isEqualTo("Doe");
        assertThat(userEntity.getPassword()).isEqualTo(encodedPassword);
        assertThat(userEntity.getRoles()).isEqualTo(Roles.ROLE_USER);
    }

    @Test
    void should_map_to_entity_from_oauth2() {
        // GIVEN
        String email = "oauth@example.com";
        String name = "OAuth";
        String surname = "User";

        // WHEN
        UserEntity userEntity = userMapper.toEntityFromOAuth2(email, name, surname);

        // THEN
        assertThat(userEntity.getEmail()).isEqualTo(email);
        assertThat(userEntity.getName()).isEqualTo(name);
        assertThat(userEntity.getSurname()).isEqualTo(surname);
        assertThat(userEntity.getPassword()).isEmpty();
        assertThat(userEntity.getRoles()).isEqualTo(Roles.ROLE_USER);
    }

    @Test
    void should_map_from_entity_to_response_dto() {
        // GIVEN
        UserEntity userEntity = new UserEntity("user@example.com", "John", "Doe", "password123");
        userEntity.changeRoleToAccountant();

        // WHEN
        UserResponseDto responseDto = userMapper.fromEntity(userEntity);

        // THEN
        assertThat(responseDto.getEmail()).isEqualTo("user@example.com");
        assertThat(responseDto.getName()).isEqualTo("John");
        assertThat(responseDto.getSurname()).isEqualTo("Doe");
        assertThat(responseDto.getRoles()).isEqualTo(Roles.ROLE_ACCOUNTANT);
    }
}
