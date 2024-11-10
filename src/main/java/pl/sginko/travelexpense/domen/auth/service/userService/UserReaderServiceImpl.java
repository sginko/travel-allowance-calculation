package pl.sginko.travelexpense.domen.auth.service.userService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.domen.auth.entity.UserEntity;
import pl.sginko.travelexpense.domen.auth.exception.UserException;
import pl.sginko.travelexpense.domen.auth.repository.UserReaderRepository;

@AllArgsConstructor
@Service
public class UserReaderServiceImpl implements UserReaderService {
    private final UserReaderRepository userReaderRepository;

    @Override
    public UserEntity findUserByEmail(String email) {
        return userReaderRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Can not find user with this email: " + email));
    }
}
