package pl.sginko.travelexpense.domen.auth.service.userService;

import pl.sginko.travelexpense.domen.auth.entity.UserEntity;

public interface UserReaderService {

    UserEntity findUserByEmail(String email);
}
