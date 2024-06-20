package pl.sginko.travelexpense.model.employee.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.model.employee.EmployeeException;
import pl.sginko.travelexpense.model.employee.entity.EmployeeEntity;
import pl.sginko.travelexpense.model.employee.repository.EmployeeReaderRepository;

@Service
public class EmployeeReaderServiceImpl implements EmployeeReaderService {
    private final EmployeeReaderRepository employeeReaderRepository;

    public EmployeeReaderServiceImpl(EmployeeReaderRepository employeeReaderRepository) {
        this.employeeReaderRepository = employeeReaderRepository;
    }

    @Override
    public EmployeeEntity findEmployeeByPesel(Long pesel) {
        return employeeReaderRepository.findByPesel(pesel)
                .orElseThrow(() -> new EmployeeException("Don't find employee with this pesel: " + pesel));
    }
}
