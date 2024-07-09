//package pl.sginko.travelexpense.controller.user;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import pl.sginko.travelexpense.logic.user.model.dto.UserRequestDto;
//import pl.sginko.travelexpense.logic.user.model.dto.UserResponseDto;
//import pl.sginko.travelexpense.logic.user.service.UserService;
//
//@AllArgsConstructor
//@Controller
//@RequestMapping("/api/v1/employees")
//public class UserControllerThymeleaf {
//    private final UserService userService;
//
//    @GetMapping("/search")
//    public String searchEmployeeForm(@RequestParam(value = "redirect", required = false) String redirect, Model model) {
//        model.addAttribute("redirect", redirect);
//        return "employee-search";
//    }
//
//    @PostMapping("/search")
//    public String searchEmployee(@RequestParam("pesel") Long pesel, @RequestParam(value = "redirect", required = false) String redirect, Model model) {
//        try {
//            UserResponseDto employee = userService.findUserByPesel(pesel);
//            if (redirect != null) {
//                return "redirect:" + redirect + "?pesel=" + pesel;
//            }
//            model.addAttribute("pesel", pesel);
//            return "redirect:/api/v1/travels?pesel=" + pesel;
//        } catch (Exception e) {
//            model.addAttribute("pesel", pesel);
//            model.addAttribute("redirect", redirect);
//            return "employee-not-found";
//        }
//    }
//
//    @GetMapping("/add")
//    public String addEmployeeForm(@RequestParam(value = "pesel", required = false) Long pesel, @RequestParam(value = "redirect", required = false) String redirect, Model model) {
//        model.addAttribute("pesel", pesel);
//        model.addAttribute("redirect", redirect);
//        return "employee-add";
//    }
//
//    @PostMapping("/add")
//    public String addEmployee(@ModelAttribute UserRequestDto userRequestDto, @RequestParam(value = "redirect", required = false) String redirect) {
//        userService.addUser(userRequestDto);
//        if (redirect != null) {
//            return "redirect:" + redirect + "?pesel=" + userRequestDto.getPesel();
//        }
//        return "redirect:/api/v1/travels?pesel=" + userRequestDto.getPesel();
//    }
//}
