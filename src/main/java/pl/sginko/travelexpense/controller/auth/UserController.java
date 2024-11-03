package pl.sginko.travelexpense.controller.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.auth.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.auth.dto.UserResponseDto;
import pl.sginko.travelexpense.logic.auth.service.userService.UserService;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/new-user")
    public void addUser(@RequestBody UserRequestDto userRequestDto) {
        userService.addUser(userRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all-users")
    public List<UserResponseDto> findAllUsers() {
        return userService.findAllUser();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{email}/change-role")
    public void changeUserRoleToAccountant(@PathVariable("email") String email) {
        userService.changeUserRoleToAccountant(email);
    }
}
