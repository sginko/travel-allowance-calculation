package pl.sginko.travelexpense.model.employee.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.sginko.travelexpense.logic.user.model.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.user.model.dto.UserResponseDto;
import pl.sginko.travelexpense.logic.user.mapper.UserMapper;
import pl.sginko.travelexpense.logic.user.repository.UserRepository;
import pl.sginko.travelexpense.logic.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void should_add_employee_() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        UserRequestDto userRequestDto = new UserRequestDto(pesel, firstName, secondName, position);

        //WHEN
        userService.addUser(userRequestDto);

        //THEN
        assertThat(userRepository.findAll()).isNotEmpty();
//        assertThat(employeeRepository.findAll().getFirst()).isEqualTo(employeeMapper.toEntity(employeeRequestDto));
    }

    @Test
    void should_find_all_employee() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        UserRequestDto userRequestDto = new UserRequestDto(pesel, firstName, secondName, position);
        userService.addUser(userRequestDto);

        //WHEN
        List<UserResponseDto> allEmployee = userService.findAllUser();

        //THEN
        assertThat(allEmployee).isNotEmpty();
    }

    @Test
    void should_find_employee_by_pesel() {
        //GIVEN
        Long pesel = 90010101001L;
        String firstName = "name";
        String secondName = "surname";
        String position = "position";
        UserRequestDto userRequestDto = new UserRequestDto(pesel, firstName, secondName, position);
        userService.addUser(userRequestDto);

        //WHEN
        UserResponseDto employeeByPesel = userService.findUserByPesel(pesel);

        //THEN
        assertThat(employeeByPesel.getPesel()).isEqualTo(pesel);
    }
}