package pl.sginko.travelexpense.controller.employee;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.employee.dto.EmployeeRequestDto;
import pl.sginko.travelexpense.logic.employee.service.EmployeeService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/employees")
public class UserController {
    private final EmployeeService userService;

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public EmployeeRequestDto addEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
//        employeeService.addEmployee(employeeRequestDto);
//        return employeeRequestDto;
//    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addEmployee(@RequestBody EmployeeRequestDto userRequestDto) {
        userService.addEmployee(userRequestDto);
    }
}
