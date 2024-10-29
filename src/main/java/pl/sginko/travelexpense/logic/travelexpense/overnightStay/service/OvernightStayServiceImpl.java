package pl.sginko.travelexpense.logic.travelexpense.overnightStay.service;

import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.dto.OvernightStayDto;
import pl.sginko.travelexpense.logic.travelexpense.overnightStay.entity.OvernightStayEntity;

@Service
public class OvernightStayServiceImpl implements OvernightStayService {

    @Override
    public OvernightStayEntity createOvernightStayEntity(OvernightStayDto overnightStayDto, OvernightStayEntity overnightStayEntity) {
        overnightStayEntity.calculateOvernightStayAmounts();
        return overnightStayEntity;
    }

//    @Override
//    public BigDecimal calculateOvernightStay(final TravelRequestDto travelRequestDto) {
////        BigDecimal amountOfOvernightStayWithoutInvoice = calculateAmountOfOvernightStayWithoutInvoice(travelRequestDto);
////        BigDecimal amountOfOvernightStayWithInvoice = calculateAmountOfOvernightStayWithInvoice(travelRequestDto);
//        return amountOfOvernightStayWithInvoice.add(amountOfOvernightStayWithoutInvoice);
//    }

}
