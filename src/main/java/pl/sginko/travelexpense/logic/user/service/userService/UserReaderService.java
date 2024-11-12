package pl.sginko.travelexpense.logic.user.service.userService;

import pl.sginko.travelexpense.logic.user.entity.UserEntity;

public interface UserReaderService {

    UserEntity findUserByEmail(String email);
}
