package pl.sginko.travelexpense.model.employee.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.model.employee.EmployeeException;
import pl.sginko.travelexpense.model.employee.dto.EmployeeRequestDto;
import pl.sginko.travelexpense.model.employee.dto.EmployeeResponseDto;
import pl.sginko.travelexpense.model.employee.entity.EmployeeEntity;
import pl.sginko.travelexpense.model.employee.mapper.EmployeeMapper;
import pl.sginko.travelexpense.model.employee.repository.EmployeeRepository;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public void addEmployee(EmployeeRequestDto requestDto) {
        employeeRepository.save(employeeMapper.toEntity(requestDto));
    }

    @Override
    public List<EmployeeResponseDto> findAllEmployee() {
        return employeeRepository.findAll().stream()
                .map(entity -> employeeMapper.fromEntity(entity))
                .toList();
    }

    @Override
    public EmployeeResponseDto findEmployeeByPesel(Long pesel) {
        EmployeeEntity employeeEntity = employeeRepository.findByPesel(pesel)
                .orElseThrow(() -> new EmployeeException("Don't find employee with this pesel: " + pesel));
        return employeeMapper.fromEntity(employeeEntity);
    }
}