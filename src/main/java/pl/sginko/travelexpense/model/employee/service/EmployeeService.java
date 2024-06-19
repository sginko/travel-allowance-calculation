package pl.sginko.travelexpense.model.employee.service;

import pl.sginko.travelexpense.model.employee.dto.EmployeeRequestDto;
import pl.sginko.travelexpense.model.employee.dto.EmployeeResponseDto;

import java.util.List;

public interface EmployeeService {
    void addEmployee(EmployeeRequestDto requestDto);

    List<EmployeeResponseDto> findAllEmployee();

    EmployeeResponseDto findEmployeeByPesel(Long pesel);
}
