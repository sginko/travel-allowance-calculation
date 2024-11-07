package pl.sginko.travelexpense.logic.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.logic.auth.entity.Roles;
import pl.sginko.travelexpense.logic.auth.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserReaderRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findAllByRoles(Roles role);
}
