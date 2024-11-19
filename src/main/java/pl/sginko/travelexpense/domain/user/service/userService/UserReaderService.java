package pl.sginko.travelexpense.domain.user.service.userService;

import pl.sginko.travelexpense.domain.user.entity.UserEntity;

public interface UserReaderService {

    UserEntity findUserByEmail(String email);
}
