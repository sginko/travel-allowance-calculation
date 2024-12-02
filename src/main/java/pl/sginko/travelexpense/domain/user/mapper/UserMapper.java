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

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.domain.user.dto.UserRequestDto;
import pl.sginko.travelexpense.domain.user.dto.UserResponseDto;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;

@AllArgsConstructor
@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserEntity toEntity(UserRequestDto requestDto) {
        return new UserEntity(requestDto.getEmail(), requestDto.getName(), requestDto.getSurname(),
                passwordEncoder.encode(requestDto.getPassword()));
    }

    public UserEntity toEntityFromOAuth2(String email, String name, String surname) {
        return new UserEntity(email, name, surname, "");
    }

    public UserResponseDto fromEntity(UserEntity entity) {
        return new UserResponseDto(entity.getEmail(), entity.getName(), entity.getSurname(), entity.getRoles());
    }
}
