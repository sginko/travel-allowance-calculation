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
package pl.sginko.travelexpense.domain.travelReport.service.travelReport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.common.eventPublisher.EventPublisher;
import pl.sginko.travelexpense.common.util.AuthenticationUtil;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportEditDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportRequestDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportSubmissionResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.*;
import pl.sginko.travelexpense.domain.travelReport.exception.TravelReportException;
import pl.sginko.travelexpense.domain.travelReport.mapper.TravelReportMapper;
import pl.sginko.travelexpense.domain.travelReport.repository.TravelReportRepository;
import pl.sginko.travelexpense.domain.travelReport.service.diet.DietService;
import pl.sginko.travelexpense.domain.travelReport.service.overnightStay.OvernightStayService;
import pl.sginko.travelexpense.domain.travelReport.service.transportCost.TransportCostService;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;
import pl.sginko.travelexpense.domain.user.service.userService.UserReaderService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TravelReportServiceImpl implements TravelReportService {
    private final TravelReportRepository travelReportRepository;
    private final TravelReportMapper travelReportMapper;
    private final DietService dietService;
    private final OvernightStayService overnightStayService;
    private final TransportCostService transportCostService;
    private final UserReaderService userReaderService;
    private final ObjectMapper objectMapper;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public TravelReportSubmissionResponseDto createTravelExpenseReport(final TravelReportRequestDto travelReportRequestDto) {
        String email = AuthenticationUtil.getCurrentUserEmail();
        UserEntity currentUser = userReaderService.findUserByEmail(email);

        TravelReportEntity travelReportEntity = travelReportMapper.toTravelEntity(travelReportRequestDto, currentUser);

        travelReportEntity.updateStatus(TravelReportStatus.SUBMITTED);

        DietEntity dietEntity = dietService.createDietEntity(travelReportRequestDto.getDietDto(), travelReportEntity);
        travelReportEntity.setDietDetails(dietEntity);

        OvernightStayEntity overnightStayEntity = overnightStayService.createOvernightStayEntity(travelReportRequestDto.getOvernightStayDto(), travelReportEntity);
        travelReportEntity.setOvernightStayDetails(overnightStayEntity);

        TransportCostEntity transportCostEntity = transportCostService.createTransportCostEntity(travelReportRequestDto.getTransportCostDto(), travelReportEntity);
        travelReportEntity.setTransportCostDetails(transportCostEntity);

        travelReportEntity.calculateTotalAmount();

        travelReportRepository.save(travelReportEntity);

        publishTravelReportSubmissionEvent(travelReportEntity, currentUser);

        return travelReportMapper.toTravelSubmissionResponseDto(travelReportEntity);
    }

    @Override
    public List<TravelReportResponseDto> getUserTravelExpenseReports() {
        String email = AuthenticationUtil.getCurrentUserEmail();

        List<TravelReportEntity> allByUserEntityEmail = travelReportRepository.findAllByUserEntity_Email(email);

        return allByUserEntityEmail.stream()
                .map(entity -> travelReportMapper.toResponseDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public TravelReportSubmissionResponseDto getTravelExpenseReportById(UUID techId) {
        TravelReportEntity travelReportEntity = getTravelEntity(techId);

        return travelReportMapper.toTravelSubmissionResponseDto(travelReportEntity);
    }

    @Override
    @Transactional
    public void updateTravelExpenseReportById(UUID techId, JsonPatch patch) {
        TravelReportEntity travelReportEntity = getTravelEntity(techId);
        TravelReportEditDto travelReportEditDto = travelReportMapper.toTravelEditDto(travelReportEntity);

        try {
            JsonNode jsonNode = objectMapper.convertValue(travelReportEditDto, JsonNode.class);
            JsonNode patched = patch.apply(jsonNode);
            TravelReportEditDto travelEdited = objectMapper.treeToValue(patched, TravelReportEditDto.class);

            travelReportEntity.updateTravelDetails(travelEdited);

        } catch (JsonPatchException | JsonProcessingException e) {
            throw new TravelReportException("Error update travel ", e);
        }
    }

    @Scheduled(cron = "${cleanup.old-reports.cron}")
    @Transactional
    public void cleanupOldReports() {
        LocalDate cutoffDate = LocalDate.now().minusDays(1);

        List<TravelReportEntity> oldReports = travelReportRepository.findByStartDateBefore(cutoffDate);

        oldReports.forEach(entity -> travelReportRepository.delete(entity));

        System.out.println("Old reports was deleted.");
    }

    private TravelReportEntity getTravelEntity(UUID techId) {
        String email = AuthenticationUtil.getCurrentUserEmail();

        return travelReportRepository.findByTechIdAndUserEntity_Email(techId, email)
                .orElseThrow(() -> new TravelReportException("Travel not found"));
    }

    private void publishTravelReportSubmissionEvent(TravelReportEntity travelReportEntity, UserEntity currentUser) {
        eventPublisher.publishTravelReportSubmissionEvent(
                travelReportEntity.getTechId(),
                currentUser.getEmail(),
                TravelReportStatus.SUBMITTED);
    }
}
