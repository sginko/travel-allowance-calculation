package pl.sginko.travelallowance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sginko.travelallowance.model.Dto.TravelRequestDto;
import pl.sginko.travelallowance.model.Dto.TravelResponseDto;
import pl.sginko.travelallowance.service.TravelService;

//@RestController
@Controller
@RequestMapping("/api/v1/travels")
class TravelController {
    private final TravelService travelService;

    TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping
    public String getTravelForm() {
        return "travel-calculator";
    }


    @PostMapping
    public String calculateTravelExpenses(@ModelAttribute TravelRequestDto travelRequestDto, Model model) {
        TravelResponseDto travelResponseDto = travelService.calculateTravelExpenses(travelRequestDto);
        model.addAttribute("travelResponse", travelResponseDto);
        return "results";
    }
}