package pl.sginko.travelexpense.model.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.model.employee.entity.EmployeeEntity;

import java.util.Optional;

public interface EmployeeReaderRepository extends JpaRepository<EmployeeEntity, Long> {
    Optional<EmployeeEntity> findByPesel(Long Pesel);
}
