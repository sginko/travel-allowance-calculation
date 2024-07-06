package pl.sginko.travelexpense.model.employee.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sginko.travelexpense.logic.employee.model.dto.EmployeeRequestDto;
import pl.sginko.travelexpense.logic.employee.model.dto.EmployeeResponseDto;
import pl.sginko.travelexpense.logic.employee.mapper.EmployeeMapper;
import pl.sginko.travelexpense.logic.employee.repository.EmployeeRepository;
import pl.sginko.travelexpense.logic.employee.service.EmployeeService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    EmployeeMapper employeeMapper;

    @AfterEach
    void tearDown() {
        employeeRepository.deleteAll();
    }

    @Test
    void should_add_employee_() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);

        //WHEN
        employeeService.addEmployee(employeeRequestDto);

        //THEN
        assertThat(employeeRepository.findAll()).isNotEmpty();
//        assertThat(employeeRepository.findAll().getFirst()).isEqualTo(employeeMapper.toEntity(employeeRequestDto));
    }

    @Test
    void should_find_all_employee() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        //WHEN
        List<EmployeeResponseDto> allEmployee = employeeService.findAllEmployee();

        //THEN
        assertThat(allEmployee).isNotEmpty();
    }

    @Test
    void should_find_employee_by_pesel() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        //WHEN
        EmployeeResponseDto employeeByPesel = employeeService.findEmployeeByPesel(pesel);

        //THEN
        assertThat(employeeByPesel.getPesel()).isEqualTo(pesel);
    }
}