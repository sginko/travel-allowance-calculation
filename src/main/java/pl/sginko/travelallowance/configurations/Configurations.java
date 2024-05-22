package pl.sginko.travelallowance.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration
public class Configurations {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
