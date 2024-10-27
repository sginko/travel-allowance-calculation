package pl.sginko.travelexpense.logic.auth.service.userService;


import pl.sginko.travelexpense.logic.auth.entity.UserEntity;

public interface UserReaderService {

    UserEntity findUserByEmail(String email);
}
