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
        return new UserResponseDto(entity.getEmail(), entity.getName(), entity.getSurname(), entity.getUserRoles());
    }
}
