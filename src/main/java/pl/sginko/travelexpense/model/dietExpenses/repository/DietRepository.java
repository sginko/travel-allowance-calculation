package pl.sginko.travelexpense.model.dietExpenses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sginko.travelexpense.model.dietExpenses.entity.DietExpensesEntity;

public interface DietRepository extends JpaRepository<DietExpensesEntity, Long> {
}
