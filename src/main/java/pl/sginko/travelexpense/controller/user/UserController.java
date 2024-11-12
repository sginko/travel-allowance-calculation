package pl.sginko.travelexpense.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.logic.user.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.user.dto.UserResponseDto;
import pl.sginko.travelexpense.logic.user.service.userService.UserService;

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
    @PatchMapping("/{email}/change-role-to-accountant")
    public void changeUserRoleToAccountant(@PathVariable("email") String email) {
        userService.changeUserRoleToAccountant(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{email}/change-role-to-manager")
    public void changeUserRoleToManager(@PathVariable("email") String email) {
        userService.changeUserRoleToManager(email);
    }
}
