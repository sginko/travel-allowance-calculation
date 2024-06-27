package pl.sginko.travelexpense.model.travel.service;

import pl.sginko.travelexpense.model.travel.dto.TravelRequestDto;

public interface TravelService {

    void addTravel(TravelRequestDto requestDto);
}
