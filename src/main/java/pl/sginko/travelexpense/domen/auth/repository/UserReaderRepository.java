package pl.sginko.travelexpense.domen.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.domen.auth.entity.UserEntity;

import java.util.Optional;

public interface UserReaderRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
}
