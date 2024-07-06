package pl.sginko.travelexpense.logic.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.logic.employee.model.entity.EmployeeEntity;

import java.util.Optional;

public interface EmployeeReaderRepository extends JpaRepository<EmployeeEntity, Long> {
    Optional<EmployeeEntity> findByPesel(Long Pesel);
}
