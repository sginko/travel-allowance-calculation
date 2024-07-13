package pl.sginko.travelexpense.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.user.model.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.user.service.UserService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/employees")
public class UserController {
    private final UserService userService;

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public EmployeeRequestDto addEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
//        employeeService.addEmployee(employeeRequestDto);
//        return employeeRequestDto;
//    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addEmployee(@RequestBody UserRequestDto userRequestDto) {
        userService.addUser(userRequestDto);
    }
}
