package pl.sginko.travelexpense.model.employee.service;

import pl.sginko.travelexpense.model.employee.entity.EmployeeEntity;

public interface EmployeeReaderService {
    EmployeeEntity findEmployeeByPesel(Long pesel);
}
