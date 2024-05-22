package pl.sginko.travelallowance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelallowance.model.Dto.TravelRequestDto;
import pl.sginko.travelallowance.model.Dto.TravelResponseDto;
import pl.sginko.travelallowance.model.Entity.TravelEntity;
import pl.sginko.travelallowance.service.TravelService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/travels")
class TravelController {
    private final TravelService travelService;


    TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @PostMapping
    public TravelResponseDto addTravelExpenses(@RequestBody TravelRequestDto travelRequestDto){
        TravelResponseDto travelResponseDto = travelService.addTravelExpenses(travelRequestDto);
        return travelResponseDto;
    }

    @GetMapping
    public TravelResponseDto getTravelExpenses(){

        return null;
    }
}
