package pl.sginko.travelexpense.controller.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.user.model.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.user.service.UserService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        userService.addUser(userRequestDto);
    }
}
