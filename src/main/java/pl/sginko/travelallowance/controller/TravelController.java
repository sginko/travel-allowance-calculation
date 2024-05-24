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

//    @PostMapping
//    public TravelResponseDto addTravelExpenses(@RequestBody TravelRequestDto travelRequestDto){
//        TravelResponseDto travelResponseDto = travelService.addTravelExpenses(travelRequestDto);
//        return travelResponseDto;

//    }

    @GetMapping
    public String getTravelForm() {
        return "travel-calculator";
    }

    @ResponseBody
    @PostMapping
    public String addTravelExpenses(@ModelAttribute TravelRequestDto travelRequestDto, Model model) {
        TravelResponseDto travelResponseDto = travelService.addTravelExpenses(travelRequestDto);
        model.addAttribute("travelResponse", travelResponseDto);
        return "results";
    }
}