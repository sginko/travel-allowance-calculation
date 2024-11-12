package pl.sginko.travelexpense.logic.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.logic.user.entity.Roles;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserResponseDto {
    private String email;
    private String name;
    private String surname;
    private Roles roles;
}
