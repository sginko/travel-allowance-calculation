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
package pl.sginko.travelexpense.domain.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sginko.travelexpense.domain.user.dto.UserRequestDto;
import pl.sginko.travelexpense.domain.user.dto.UserResponseDto;
import pl.sginko.travelexpense.domain.user.entity.Roles;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;

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
        UserRequestDto requestDto = new UserRequestDto("user@test.com", "name", "surname", "password");
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode("password")).thenReturn(encodedPassword);

        // WHEN
        UserEntity userEntity = userMapper.toEntity(requestDto);

        // THEN
        assertThat(userEntity.getEmail()).isEqualTo("user@test.com");
        assertThat(userEntity.getName()).isEqualTo("name");
        assertThat(userEntity.getSurname()).isEqualTo("surname");
        assertThat(userEntity.getPassword()).isEqualTo(encodedPassword);
        assertThat(userEntity.getRoles()).isEqualTo(Roles.ROLE_USER);
    }

    @Test
    void should_map_to_entity_from_oauth2() {
        // GIVEN
        String email = "oauth@test.com";
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
        UserEntity userEntity = new UserEntity("user@test.com", "name", "surname", "password");
        userEntity.changeRoleToAccountant();

        // WHEN
        UserResponseDto responseDto = userMapper.fromEntity(userEntity);

        // THEN
        assertThat(responseDto.getEmail()).isEqualTo("user@test.com");
        assertThat(responseDto.getName()).isEqualTo("name");
        assertThat(responseDto.getSurname()).isEqualTo("surname");
        assertThat(responseDto.getRoles()).isEqualTo(Roles.ROLE_ACCOUNTANT);
    }
}
