package pl.sginko.travelallowance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sginko.travelallowance.model.Dto.TravelResponseDto;
import pl.sginko.travelallowance.model.Entity.TravelEntity;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/travel-allowances")
class TravelController {

    @GetMapping
    public TravelResponseDto getTravelExpenses(){

        return null;
    }
}
