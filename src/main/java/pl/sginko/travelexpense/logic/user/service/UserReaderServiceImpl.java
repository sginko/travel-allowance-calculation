package pl.sginko.travelexpense.logic.user.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.user.exception.UserException;
import pl.sginko.travelexpense.logic.user.model.entity.UserEntity;
import pl.sginko.travelexpense.logic.user.repository.UserReaderRepository;

@Service
public class UserReaderServiceImpl implements UserReaderService {
    private final UserReaderRepository userReaderRepository;

    public UserReaderServiceImpl(UserReaderRepository userReaderRepository) {
        this.userReaderRepository = userReaderRepository;
    }

    @Override
    public UserEntity findUserByPesel(Long pesel) {
        return userReaderRepository.findByPesel(pesel)
                .orElseThrow(() -> new UserException("Can not find employee with this pesel: " + pesel));
    }
}
