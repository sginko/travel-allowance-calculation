package pl.sginko.travelexpense.logic.user.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.user.exception.UserException;
import pl.sginko.travelexpense.logic.user.model.dto.UserRequestDto;
import pl.sginko.travelexpense.logic.user.model.dto.UserResponseDto;
import pl.sginko.travelexpense.logic.user.model.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.mapper.UserMapper;
import pl.sginko.travelexpense.logic.user.repository.UserRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void addUser(UserRequestDto requestDto) {
        userRepository.save(userMapper.toEntity(requestDto));
    }

    @Override
    public List<UserResponseDto> findAllUser() {
        return userRepository.findAll().stream()
                .map(entity -> userMapper.fromEntity(entity))
                .toList();
    }

    @Override
    public UserResponseDto findUserByPesel(Long pesel) {
        UserEntity userEntity = userRepository.findByPesel(pesel)
                .orElseThrow(() -> new UserException("Can not find user with this pesel: " + pesel));
        return userMapper.fromEntity(userEntity);
    }
}