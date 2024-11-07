package pl.sginko.travelexpense.logic.auth.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.auth.entity.Roles;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserResponseDto {
    private String email;
    private String name;
    private String surname;
    private Roles roles;
}
