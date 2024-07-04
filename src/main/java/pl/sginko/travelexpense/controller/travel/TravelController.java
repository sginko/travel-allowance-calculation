package pl.sginko.travelexpense.controller.travel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelRequestDto;
import pl.sginko.travelexpense.logic.travel.model.dto.TravelResponseDto;
import pl.sginko.travelexpense.logic.travel.service.TravelService;

@Controller
@RequestMapping("/api/v1/travels")
class TravelController {
    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @PostMapping("/calculate")
    public TravelResponseDto calculateTravelExpenses(@RequestBody TravelRequestDto travelRequestDto) {
        return travelService.calculateTravelExpenses(travelRequestDto);
    }
}
