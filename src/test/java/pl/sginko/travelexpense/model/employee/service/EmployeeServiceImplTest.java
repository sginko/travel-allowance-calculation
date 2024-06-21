package pl.sginko.travelexpense.model.employee.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sginko.travelexpense.model.employee.dto.EmployeeRequestDto;
import pl.sginko.travelexpense.model.employee.dto.EmployeeResponseDto;
import pl.sginko.travelexpense.model.employee.mapper.EmployeeMapper;
import pl.sginko.travelexpense.model.employee.repository.EmployeeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        //given
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);

        //when
        employeeService.addEmployee(employeeRequestDto);

        //then
        assertThat(employeeRepository.findAll()).isNotEmpty();
//        assertThat(employeeRepository.findAll().getFirst()).isEqualTo(employeeMapper.toEntity(employeeRequestDto));
    }

    @Test
    void should_find_all_employee() {
        //given
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        //when
        List<EmployeeResponseDto> allEmployee = employeeService.findAllEmployee();

        //then
        assertThat(allEmployee).isNotEmpty();
    }

    @Test
    void should_find_employee_by_pesel() {
        //given
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        EmployeeRequestDto employeeRequestDto = new EmployeeRequestDto(pesel, firstName, secondName, position);
        employeeService.addEmployee(employeeRequestDto);

        //when
        EmployeeResponseDto employeeByPesel = employeeService.findEmployeeByPesel(pesel);

        //then
        assertThat(employeeByPesel.getPesel()).isEqualTo(pesel);
    }
}