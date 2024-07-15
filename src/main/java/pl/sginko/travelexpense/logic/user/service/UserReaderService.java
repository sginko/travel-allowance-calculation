package pl.sginko.travelexpense.logic.user.service;

import pl.sginko.travelexpense.logic.user.model.entity.UserEntity;

public interface UserReaderService {

    UserEntity findUserByPesel(Long pesel);
}
