package pl.sginko.travelexpense.logic.auth.service.userService;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;
import pl.sginko.travelexpense.logic.auth.exception.UserException;
import pl.sginko.travelexpense.logic.auth.repository.UserReaderRepository;
//import pl.sginko.travelexpense.logic.user.exception.UserException;

@Service
public class UserReaderServiceImpl implements UserReaderService {
    private final UserReaderRepository userReaderRepository;

    public UserReaderServiceImpl(UserReaderRepository userReaderRepository) {
        this.userReaderRepository = userReaderRepository;
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userReaderRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Can not find employee with this email: " + email));
    }
}
