//package pl.sginko.travelexpense.logic.user.mapper;
//
//import org.springframework.stereotype.Component;
//import pl.sginko.travelexpense.logic.user.model.dto.UserRequestDto;
//import pl.sginko.travelexpense.logic.user.model.dto.UserResponseDto;
//import pl.sginko.travelexpense.logic.user.model.entity.UserEntity;
//
//@Component
//public class UserMapper {
//
//    public UserEntity toEntity(UserRequestDto requestDto) {
//        return new UserEntity(requestDto.getPesel(), requestDto.getFirstName(),
//                requestDto.getSecondName(), requestDto.getPosition());
//    }
//
//    public UserResponseDto fromEntity(UserEntity entity) {
//        return new UserResponseDto(entity.getPesel(), entity.getFirstName(),
//                entity.getSecondName(), entity.getPosition());
//    }
//}
