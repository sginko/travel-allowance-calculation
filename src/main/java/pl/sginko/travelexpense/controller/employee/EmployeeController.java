package pl.sginko.travelexpense.controller.employee;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.model.employee.dto.EmployeeRequestDto;
import pl.sginko.travelexpense.model.employee.dto.EmployeeResponseDto;
import pl.sginko.travelexpense.model.employee.service.EmployeeService;

@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/search")
    public String searchEmployeeForm() {
        return "employee-search";
    }

    @PostMapping("/search")
    public String searchEmployee(@RequestParam("pesel") Long pesel, Model model) {
        try {
            EmployeeResponseDto employee = employeeService.findEmployeeByPesel(pesel);
            model.addAttribute("employee", employee);
            return "redirect:/api/v1/travels";
        } catch (Exception e) {
            model.addAttribute("pesel", pesel);
            return "employee-not-found";
        }
    }

    @GetMapping("/add")
    public String addEmployeeForm(@RequestParam("pesel") Long pesel, Model model) {
        model.addAttribute("pesel", pesel);
        return "employee-add";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute EmployeeRequestDto employeeRequestDto) {
        employeeService.addEmployee(employeeRequestDto);
        return "redirect:/api/v1/travels";
    }
}
