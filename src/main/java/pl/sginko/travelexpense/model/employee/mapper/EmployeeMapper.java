package pl.sginko.travelexpense.model.employee.mapper;

import org.springframework.stereotype.Component;
import pl.sginko.travelexpense.model.employee.dto.EmployeeRequestDto;
import pl.sginko.travelexpense.model.employee.dto.EmployeeResponseDto;
import pl.sginko.travelexpense.model.employee.entity.EmployeeEntity;

@Component
public class EmployeeMapper {

    public EmployeeEntity toEntity(EmployeeRequestDto requestDto) {
        return new EmployeeEntity(requestDto.getPesel(), requestDto.getFirstName(),
                requestDto.getSecondName(), requestDto.getPosition());
    }

    public EmployeeResponseDto fromEntity(EmployeeEntity entity) {
        return new EmployeeResponseDto(entity.getPesel(), entity.getFirstName(),
                entity.getSecondName(), entity.getPosition());
    }
}
