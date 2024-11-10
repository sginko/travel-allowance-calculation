package pl.sginko.travelexpense.domen.auth.service.userDetailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.sginko.travelexpense.domen.auth.entity.Roles;
import pl.sginko.travelexpense.domen.auth.entity.UserEntity;
import pl.sginko.travelexpense.domen.auth.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity("test@example.com", "John", "Doe", "encodedPassword");
    }

    @Test
    void should_return_user_details_when_user_exists() {
        // GIVEN
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // WHEN
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // THEN
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(userEntity.getEmail());
        assertThat(userDetails.getPassword()).isEqualTo(userEntity.getPassword());
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly(Roles.ROLE_USER.name());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void should_throw_username_not_found_exception_when_user_does_not_exist() {
        // GIVEN
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // WHEN
        Executable executable = () -> userDetailsService.loadUserByUsername(email);

        // THEN
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, executable);
        assertThat(exception.getMessage()).isEqualTo("User not found with email: " + email);

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void should_return_correct_authorities_when_user_has_different_role() {
        // GIVEN
        String email = "manager@example.com";
        UserEntity managerUser = new UserEntity("manager@example.com", "Jane", "Smith", "encodedPassword");
        managerUser.changeRoleToManager();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(managerUser));

        // WHEN
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // THEN
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(managerUser.getEmail());
        assertThat(userDetails.getPassword()).isEqualTo(managerUser.getPassword());
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactly(Roles.ROLE_MANAGER.name());

        verify(userRepository, times(1)).findByEmail(email);
    }
}