package pl.sginko.travelexpense.logic.auth.service.userService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.auth.entity.Roles;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.auth.exception.UserException;
import pl.sginko.travelexpense.logic.auth.repository.UserReaderRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class UserReaderServiceImpl implements UserReaderService {
    private final UserReaderRepository userReaderRepository;

    @Override
    public UserEntity findUserByEmail(String email) {
        return userReaderRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Can not find user with this email: " + email));
    }

    @Override
    public List<UserEntity> findUsersByRole(Roles role) {
        return userReaderRepository.findAllByRoles(role);
    }

//    @Override
//    public List<UserEntity> findAllUsersByRoles(Roles role) {
//        return userReaderRepository.findAllByRoles(role);
//    }
}
