package pl.sginko.travelexpense.controller.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.auth.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.auth.service.userService.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/travels")
public class UserController {
    private UserService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/new-user")
    public void addUser(@RequestBody UserRequestDto userRequestDto) {
        service.addUser(userRequestDto);
    }

    @GetMapping("/apps")
    public String pageWithLogin() {
        return "You have successfully logged in!";
    }

    @GetMapping("/site")
    public String anotherPage() {
        return "Welcome to main page!";
    }
}
