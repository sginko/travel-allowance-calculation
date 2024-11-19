package pl.sginko.travelexpense.domain.user.service.userService;

import pl.sginko.travelexpense.domain.user.dto.UserRequestDto;
import pl.sginko.travelexpense.domain.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    void addUser(UserRequestDto userRequestDto);

    void saveOAuth2User(String email, String name, String surname);

    List<UserResponseDto> findAllUser();

    void changeUserRoleToAccountant(String email);

    void changeUserRoleToManager(String email);
}
