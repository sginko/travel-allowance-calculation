package pl.sginko.travelexpense.logic.employee.service;

import pl.sginko.travelexpense.logic.employee.model.dto.EmployeeRequestDto;
import pl.sginko.travelexpense.logic.employee.model.dto.EmployeeResponseDto;

import java.util.List;

public interface EmployeeService {
    void addEmployee(EmployeeRequestDto requestDto);

    List<EmployeeResponseDto> findAllEmployee();

    EmployeeResponseDto findEmployeeByPesel(Long pesel);
}
