/*
 * Copyright 2024 Sergii Ginkota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.sginko.travelexpense.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelexpense.domain.user.dto.UserRequestDto;
import pl.sginko.travelexpense.domain.user.dto.UserResponseDto;
import pl.sginko.travelexpense.domain.user.service.userService.UserService;

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
