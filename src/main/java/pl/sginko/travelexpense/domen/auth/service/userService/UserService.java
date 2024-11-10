package pl.sginko.travelexpense.domen.auth.service.userService;

import pl.sginko.travelexpense.domen.auth.dto.UserRequestDto;
import pl.sginko.travelexpense.domen.auth.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    void addUser(UserRequestDto userRequestDto);

    void saveOAuth2User(String email, String name, String surname);

    List<UserResponseDto> findAllUser();

    void changeUserRoleToAccountant(String email);

    void changeUserRoleToManager(String email);
}
