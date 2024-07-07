package pl.sginko.travelexpense.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import pl.sginko.travelexpense.logic.diet.service.DietService;
import pl.sginko.travelexpense.logic.employee.service.EmployeeReaderService;
import pl.sginko.travelexpense.logic.overnightStay.service.OvernightStayService;
import pl.sginko.travelexpense.logic.travel.mapper.TravelMapper;
import pl.sginko.travelexpense.logic.travel.repository.TravelRepository;
import pl.sginko.travelexpense.logic.travel.service.TravelService;
import pl.sginko.travelexpense.logic.travel.service.TravelServiceImpl;

@Configuration
class Configurations {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:63342"); // Разрешить запросы с вашего фронтенда
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }

//    @Bean
//    public TravelService travelService(TravelRepository travelRepository, TravelMapper travelMapper, DietService dietService,
//                                OvernightStayService overnightStayService, EmployeeReaderService employeeReaderService) {
//        return new TravelServiceImpl(travelRepository, travelMapper, dietService, overnightStayService, employeeReaderService);
//    }
}