//package pl.sginko.travelexpense.logic.travel.service;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import pl.sginko.travelexpense.logic.diet.service.DietService;
//import pl.sginko.travelexpense.logic.employee.service.EmployeeReaderService;
//import pl.sginko.travelexpense.logic.overnightStay.service.OvernightStayService;
//import pl.sginko.travelexpense.logic.travel.mapper.TravelMapper;
//import pl.sginko.travelexpense.logic.travel.repository.TravelRepository;
//
//@Configuration
//class TravelConfiguration {
//
//    @Bean
//    TravelService travelService(TravelRepository travelRepository,
//                                TravelMapper travelMapper,
//                                DietService dietService,
//                                OvernightStayService overnightStayService,
//                                EmployeeReaderService employeeReaderService) {
//        return new TravelServiceImpl(travelRepository, travelMapper, dietService, overnightStayService, employeeReaderService);
//    }
//}
