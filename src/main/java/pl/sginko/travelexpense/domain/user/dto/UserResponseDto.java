package pl.sginko.travelexpense.domain.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.sginko.travelexpense.domain.user.entity.UserRoles;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserResponseDto {
    private String email;
    private String name;
    private String surname;
    private UserRoles userRoles;
}
