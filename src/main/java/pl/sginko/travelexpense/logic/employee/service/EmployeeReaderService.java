package pl.sginko.travelexpense.logic.employee.service;

import pl.sginko.travelexpense.logic.employee.model.entity.EmployeeEntity;

public interface EmployeeReaderService {
    EmployeeEntity findEmployeeByPesel(Long pesel);
}
