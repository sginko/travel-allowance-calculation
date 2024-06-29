package pl.sginko.travelexpense.model.overnightStayExpenses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.model.overnightStayExpenses.entity.OvernightStayExpensesEntity;

public interface OvernightStayRepository extends JpaRepository<OvernightStayExpensesEntity, Long> {
}
