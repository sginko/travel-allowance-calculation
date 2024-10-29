//package pl.sginko.travelexpense.logic.auth.service.userService;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.function.Executable;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import pl.sginko.travelexpense.logic.auth.dto.UserRequestDto;
//import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
//import pl.sginko.travelexpense.logic.auth.repository.UserReaderRepository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class UserReaderServiceTest {
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserReaderService userReaderService;
//
//    @Autowired
//    private UserReaderRepository userReaderRepository;
//
//    @AfterEach
//    void tearDown() {
//        userReaderRepository.deleteAll();
//    }
//
//    @Test
//    void should_find_user_by_email() {
//        // GIVEN
//        String email = "test@mail.com";
//        String name = "name";
//        String surname = "surname";
//        String roles = "roles";
//        UserRequestDto userRequestDto = new UserRequestDto(email, name, surname, roles);
//        userService.addUser(userRequestDto);
//
//        // WHEN
//        UserEntity userByEmail = userReaderService.findUserByEmail(email);
//
//        // THEN
//        assertThat(userByEmail.getEmail()).isEqualTo(email);
//    }
//
////    @Test
////    void should_throw_user_exception_if_user_not_find() {
////        // GIVEN
////        String email = "test@mail.com";
////        String name = "name";
////        String surname = "surname";
////        String password = "password";
////        UserRequestDto userRequestDto = new UserRequestDto(email, name, surname, password);
////        userService.addUser(userRequestDto);
////
////        // WHEN
////        Executable e = () -> userReaderService.findUserByEmail(email);
////
////        // THEN
////        assertThat(userByEmail.getEmail()).isEqualTo(email);
////    }
//}
