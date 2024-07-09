package pl.sginko.travelexpense.logic.user.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class UserRequestDto {
    @NotNull(message = "Pessel cannot be null")
    @Min(value = 0, message = "Pessel cannot be negative")
    private Long pesel;

    @NotBlank(message = "FirstName cannot be blank")
    @Size(min = 2, max = 50, message = "FirstName should be between 2 и 50 characters")
    private String firstName;

    @NotBlank(message = "SecondName cannot be blank")
    @Size(min = 2, max = 50, message = "SecondName should be between 2 и 50 characters")
    private String secondName;

    @NotBlank(message = "Position cannot be blank")
    @Size(min = 2, max = 50, message = "Position should be between 2 и 50 characters")
    private String position;
}
