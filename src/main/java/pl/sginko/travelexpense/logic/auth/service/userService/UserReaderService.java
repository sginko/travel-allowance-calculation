package pl.sginko.travelexpense.logic.auth.service.userService;

import pl.sginko.travelexpense.logic.auth.entity.Roles;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;

import java.util.List;

public interface UserReaderService {

    UserEntity findUserByEmail(String email);

//    List<UserEntity> findAllUsersByRoles(Roles role);

    List<UserEntity> findUsersByRole(Roles role);
}
