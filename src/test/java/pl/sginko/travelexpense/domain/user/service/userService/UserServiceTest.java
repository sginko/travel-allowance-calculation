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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.sginko.travelexpense.domain.user.dto.UserRequestDto;
import pl.sginko.travelexpense.domain.user.dto.UserResponseDto;
import pl.sginko.travelexpense.domain.user.entity.Roles;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;
import pl.sginko.travelexpense.domain.user.exception.UserException;
import pl.sginko.travelexpense.domain.user.exception.UserNotFoundException;
import pl.sginko.travelexpense.domain.user.mapper.UserMapper;
import pl.sginko.travelexpense.domain.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        userRequestDto = new UserRequestDto("test@test.com", "name", "surname", "password123");
        userEntity = new UserEntity("test@test.com", "name", "surname", "encodedPassword");
        userResponseDto = new UserResponseDto("test@test.com", "name", "surname", Roles.ROLE_USER);
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

    @Test
    void should_throw_exception_when_user_already_exists() {
        // GIVEN
        when(userRepository.findByEmail(userRequestDto.getEmail())).thenReturn(Optional.of(userEntity));

        // WHEN
        Executable e = () -> userService.addUser(userRequestDto);

        // THEN
        assertThrows(UserException.class, e);
        verify(userRepository, times(1)).findByEmail(userRequestDto.getEmail());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

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
        String email = "oauth@test.com";
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
        String email = "existing@test.com";
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

    @Test
    void should_throw_exception_when_changing_role_to_accountant_for_nonexistent_user() {
        // GIVEN
        String email = "nonexistent@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // WHEN
        Executable e = () -> userService.changeUserRoleToAccountant(email);

        // THEN
        assertThrows(UserNotFoundException.class, e, "Can not find user with this email: " + email);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void should_throw_exception_when_changing_role_to_manager_for_nonexistent_user() {
        // GIVEN
        String email = "nonexistent@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // WHEN
        Executable e = () -> userService.changeUserRoleToManager(email);

        // THEN
        assertThrows(UserNotFoundException.class, e, "Can not find user with this email: " + email);
        verify(userRepository, times(1)).findByEmail(email);
    }
}
