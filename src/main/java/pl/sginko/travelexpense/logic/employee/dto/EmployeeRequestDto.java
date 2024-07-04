package pl.sginko.travelexpense.logic.employee.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class EmployeeRequestDto {
    private Long pesel;
    private String firstName;
    private String secondName;
    private String position;
}
