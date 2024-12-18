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
package pl.sginko.travelexpense.domain.approval.service;

import org.jobrunr.jobs.lambdas.JobLambda;
import org.jobrunr.scheduling.JobScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.domain.approval.entity.ApprovalEntity;
import pl.sginko.travelexpense.domain.approval.entity.ApprovalStatus;
import pl.sginko.travelexpense.domain.approval.exception.ApprovalException;
import pl.sginko.travelexpense.domain.approval.repository.ApprovalRepository;
import pl.sginko.travelexpense.domain.travelReport.dto.travelReport.TravelReportResponseDto;
import pl.sginko.travelexpense.domain.travelReport.entity.*;
import pl.sginko.travelexpense.domain.travelReport.repository.TravelReportRepository;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;
import pl.sginko.travelexpense.domain.user.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ApprovalServiceIntegrationTest {
    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TravelReportRepository travelReportRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private JobScheduler jobScheduler;

    private UserEntity manager;
    private UserEntity accountant;

    @BeforeEach
    void setUp() {
        manager = new UserEntity("manager@test.com", "Manager", "User", "password");
        manager.changeRoleToManager();
        userRepository.save(manager);

        accountant = new UserEntity("accountant@test.com", "Accountant", "User", "password");
        accountant.changeRoleToAccountant();
        userRepository.save(accountant);

        doAnswer(invocation -> {
            JobLambda jobLambda = invocation.getArgument(0);
            jobLambda.run();
            return null;
        }).when(jobScheduler).enqueue(any(JobLambda.class));
    }

    @Test
    void should_return_pending_approvals() {
        // GIVEN
        TravelReportEntity travelReport = createAndSaveTravelReport("CityA", "CityB");

        // WHEN
        List<TravelReportResponseDto> pendingApprovals = approvalService.getTravelReportsForApproval(manager.getEmail());

        // THEN
        assertThat(pendingApprovals).isNotEmpty();
        assertThat(pendingApprovals).hasSize(1);
        assertThat(pendingApprovals.get(0).getFromCity()).isEqualTo("CityA");
        assertThat(pendingApprovals.get(0).getToCity()).isEqualTo("CityB");
    }

    @Test
    void should_approve_travel_successfully() {
        // GIVEN
        TravelReportEntity travelReport = createAndSaveTravelReport("CityA", "CityB");

        // WHEN
        approvalService.approveTravelReport(travelReport.getTechId(), manager.getEmail());
        approvalService.approveTravelReport(travelReport.getTechId(), accountant.getEmail());

        travelReportRepository.flush();

        // THEN
        List<ApprovalEntity> approvals = approvalRepository.findAll().stream()
                .filter(approval -> approval.getTravelReportEntity().equals(travelReport))
                .collect(Collectors.toList());
        assertThat(approvals).hasSize(2);

        ApprovalEntity managerApproval = approvals.stream()
                .filter(a -> a.getApprover().equals(manager))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Manager approval not found"));
        ApprovalEntity accountantApproval = approvals.stream()
                .filter(a -> a.getApprover().equals(accountant))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Accountant approval not found"));

        assertThat(managerApproval.getStatus()).isEqualTo(ApprovalStatus.APPROVED);
        assertThat(accountantApproval.getStatus()).isEqualTo(ApprovalStatus.APPROVED);

        TravelReportEntity updatedTravelReport = travelReportRepository.findByTechId(travelReport.getTechId())
                .orElseThrow(() -> new RuntimeException("Travel report not found"));
        assertThat(updatedTravelReport.getStatus()).isEqualTo(TravelReportStatus.APPROVED);
    }

    @Test
    void should_reject_travel_successfully() {
        // GIVEN
        TravelReportEntity travelReport = createAndSaveTravelReport("CityA", "CityB");

        // WHEN
        approvalService.approveTravelReport(travelReport.getTechId(), manager.getEmail());
        approvalService.rejectTravelReport(travelReport.getTechId(), accountant.getEmail());

        // Фиксируем изменения
        travelReportRepository.flush();

        // THEN
        List<ApprovalEntity> approvals = approvalRepository.findAll().stream()
                .filter(approval -> approval.getTravelReportEntity().equals(travelReport))
                .collect(Collectors.toList());
        assertThat(approvals).hasSize(2);

        ApprovalEntity managerApproval = approvals.stream()
                .filter(a -> a.getApprover().equals(manager))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Manager approval not found"));
        ApprovalEntity accountantApproval = approvals.stream()
                .filter(a -> a.getApprover().equals(accountant))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Accountant approval not found"));

        assertThat(managerApproval.getStatus()).isEqualTo(ApprovalStatus.APPROVED);
        assertThat(accountantApproval.getStatus()).isEqualTo(ApprovalStatus.REJECTED);

        TravelReportEntity updatedTravelReport = travelReportRepository.findByTechId(travelReport.getTechId())
                .orElseThrow(() -> new RuntimeException("Travel report not found"));
        assertThat(updatedTravelReport.getStatus()).isEqualTo(TravelReportStatus.REJECTED);
    }

    @Test
    void should_throw_exception_when_approving_already_approved_travel() {
        // GIVEN
        TravelReportEntity travelReport = createAndSaveTravelReport("CityA", "CityB");
        approvalService.approveTravelReport(travelReport.getTechId(), manager.getEmail());
        approvalService.approveTravelReport(travelReport.getTechId(), accountant.getEmail());

        // WHEN & THEN
        ApprovalException exception = assertThrows(ApprovalException.class, () ->
                approvalService.approveTravelReport(travelReport.getTechId(), manager.getEmail()));
        assertThat(exception.getMessage()).contains("Approval has already been processed");
    }

    private TravelReportEntity createAndSaveTravelReport(String fromCity, String toCity) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);

        TravelReportEntity travelReport = new TravelReportEntity(fromCity, toCity, startDate, LocalTime.of(8, 0),
                endDate, LocalTime.of(18, 0), manager, BigDecimal.valueOf(100), BigDecimal.valueOf(50));

        DietEntity dietEntity = new DietEntity(travelReport, BigDecimal.valueOf(45), 2, 1, 1);
        travelReport.setDietDetails(dietEntity);

        OvernightStayEntity overnightStayEntity = new OvernightStayEntity(travelReport, 1,
                0, BigDecimal.ZERO, false);
        travelReport.setOvernightStayDetails(overnightStayEntity);

        TransportCostEntity transportCostEntity = new TransportCostEntity(travelReport, 0,
                BigDecimal.ZERO, "Car", BigDecimal.valueOf(150), 100L,
                0L, 0L, 0L);
        travelReport.setTransportCostDetails(transportCostEntity);

        travelReport.calculateTotalAmount();

        return travelReportRepository.save(travelReport);
    }
}
