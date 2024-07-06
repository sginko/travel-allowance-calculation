package pl.sginko.travelexpense.controller.employee;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.employee.model.dto.EmployeeRequestDto;
import pl.sginko.travelexpense.logic.employee.model.dto.EmployeeResponseDto;
import pl.sginko.travelexpense.logic.employee.service.EmployeeService;

@Controller
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/search")
    public String searchEmployeeForm(@RequestParam(value = "redirect", required = false) String redirect, Model model) {
        model.addAttribute("redirect", redirect);
        return "employee-search";
    }

    @PostMapping("/search")
    public String searchEmployee(@RequestParam("pesel") Long pesel, @RequestParam(value = "redirect", required = false) String redirect, Model model) {
        try {
            EmployeeResponseDto employee = employeeService.findEmployeeByPesel(pesel);
            if (redirect != null) {
                return "redirect:" + redirect + "?pesel=" + pesel;
            }
            model.addAttribute("pesel", pesel);
            return "redirect:/api/v1/travels?pesel=" + pesel;
        } catch (Exception e) {
            model.addAttribute("pesel", pesel);
            model.addAttribute("redirect", redirect);
            return "employee-not-found";
        }
    }

    @GetMapping("/add")
    public String addEmployeeForm(@RequestParam(value = "pesel", required = false) Long pesel, @RequestParam(value = "redirect", required = false) String redirect, Model model) {
        model.addAttribute("pesel", pesel);
        model.addAttribute("redirect", redirect);
        return "employee-add";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute EmployeeRequestDto employeeRequestDto, @RequestParam(value = "redirect", required = false) String redirect) {
        employeeService.addEmployee(employeeRequestDto);
        if (redirect != null) {
            return "redirect:" + redirect + "?pesel=" + employeeRequestDto.getPesel();
        }
        return "redirect:/api/v1/travels?pesel=" + employeeRequestDto.getPesel();
    }
}
