package pl.sginko.travelexpense.logic.auth.service.userService;

//import pl.ginko.auth_faktura.logic.model.dto.UserRequestDto;

import pl.sginko.travelexpense.logic.auth.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.auth.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    void addUser(UserRequestDto userRequestDto);

    void registerOAuth2User(String email, String name, String surname);

    List<UserResponseDto> findAllUser();

    UserResponseDto findUserByEmail(String email);
}
