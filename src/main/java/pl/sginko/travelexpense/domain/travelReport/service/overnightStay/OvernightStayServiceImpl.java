/*
 * Copyright 2024 Sergii Ginkota
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.sginko.travelexpense.domain.travelReport.service.overnightStay;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sginko.travelexpense.domain.travelReport.dto.overnightStay.OvernightStayDto;
import pl.sginko.travelexpense.domain.travelReport.entity.OvernightStayEntity;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.travelReport.mapper.OvernightStayMapper;

@AllArgsConstructor
@Service
public class OvernightStayServiceImpl implements OvernightStayService {
    private final OvernightStayMapper overnightStayMapper;

    @Override
    public OvernightStayEntity createOvernightStayEntity(OvernightStayDto overnightStayDto, TravelReportEntity travelReportEntity) {
        OvernightStayEntity overnightStayEntity = overnightStayMapper.toEntity(overnightStayDto, travelReportEntity);
        return overnightStayEntity;
    }
}
