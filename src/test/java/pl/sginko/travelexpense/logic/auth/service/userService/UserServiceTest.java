//package pl.sginko.travelexpense.logic.auth.service.userService;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import pl.sginko.travelexpense.logic.auth.dto.UserRequestDto;
//import pl.sginko.travelexpense.logic.auth.dto.UserResponseDto;
//import pl.sginko.travelexpense.logic.auth.repository.UserRepository;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class UserServiceTest {
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @AfterEach
//    void tearDown() {
//        userRepository.deleteAll();
//    }
//
//    @Test
//    void should_add_user() {
//        // GIVEN
//        String email = "test@mail.com";
//        String name = "name";
//        String surname = "surname";
//        String roles = "roles";
//        UserRequestDto userRequestDto = new UserRequestDto(email, name, surname, roles);
//
//        // WHEN
//        userService.addUser(userRequestDto);
//
//        // THEN
//        assertThat(userRepository.findAll()).isNotEmpty();
//    }
//
//    @Test
//    void should_find_all_users() {
//        // GIVEN
//        String email = "test@mail.com";
//        String name = "name";
//        String surname = "surname";
//        String roles = "roles";
//        UserRequestDto userRequestDto = new UserRequestDto(email, name, surname, roles);
//        userService.addUser(userRequestDto);
//
//        // WHEN
//        List<UserResponseDto> allUsers = userService.findAllUser();
//
//        // THEN
//        assertThat(allUsers).isNotEmpty();
//    }
//}
