package pl.sginko.travelexpense.logic.auth.service.userService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.logic.auth.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.auth.dto.UserResponseDto;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.auth.exception.UserException;
import pl.sginko.travelexpense.logic.auth.mapper.UserMapper;
import pl.sginko.travelexpense.logic.auth.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void addUser(UserRequestDto userRequestDto) {
        checkUserExistsOrThrow(userRequestDto.getEmail());
        userRepository.save(userMapper.toEntity(userRequestDto));
    }

    @Override
    public void saveOAuth2User(String email, String name, String surname) {
        if (userRepository.findByEmail(email).isEmpty()) {
            UserEntity newUser = userMapper.toEntityFromOAuth2(email, name, surname);
            userRepository.save(newUser);
        }
    }

    @Override
    public List<UserResponseDto> findAllUser() {
        return userRepository.findAll().stream()
                .map(entity -> userMapper.fromEntity(entity))
                .toList();
    }

    @Transactional
    @Override
    public void changeUserRoleToAccountant(String email) {
        UserEntity userEntity = findUserByEmailOrThrow(email);
        userEntity.changeRoleToAccountant();
    }

    @Transactional
    @Override
    public void changeUserRoleToManager(String email) {
        UserEntity userEntity = findUserByEmailOrThrow(email);
        userEntity.changeRoleToManager();
    }

    private void checkUserExistsOrThrow(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserException("User with this email already exists");
        }
    }

    private UserEntity findUserByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Can not find user with this email: " + email));
    }
}
