package pl.sginko.travelexpense.logic.employee.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.employee.exception.EmployeeException;
import pl.sginko.travelexpense.logic.employee.repository.EmployeeReaderRepository;
import pl.sginko.travelexpense.logic.employee.model.entity.EmployeeEntity;

@Service
public class EmployeeReaderServiceImpl implements EmployeeReaderService {
    private final EmployeeReaderRepository employeeReaderRepository;

    public EmployeeReaderServiceImpl(EmployeeReaderRepository employeeReaderRepository) {
        this.employeeReaderRepository = employeeReaderRepository;
    }

    @Override
    public EmployeeEntity findEmployeeByPesel(Long pesel) {
        return employeeReaderRepository.findByPesel(pesel)
                .orElseThrow(() -> new EmployeeException("Can not find employee with this pesel: " + pesel));
    }
}
