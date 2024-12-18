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
package pl.sginko.travelexpense.domain.approval.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.sginko.travelexpense.domain.approval.exception.ApprovalException;
import pl.sginko.travelexpense.domain.travelReport.entity.TravelReportEntity;
import pl.sginko.travelexpense.domain.user.entity.Roles;
import pl.sginko.travelexpense.domain.user.entity.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApprovalEntityTest {
    private ApprovalEntity approvalEntity;
    private TravelReportEntity travelReportEntity;
    private UserEntity approver;

    private static final Roles DEFAULT_ROLE = Roles.ROLE_MANAGER;
    private static final ApprovalStatus INITIAL_STATUS = ApprovalStatus.PENDING;


    @BeforeEach
    void setUp() {
        approver = new UserEntity("approver@test.com", "approver", "user", "password");

        approver.changeRoleToManager();

        travelReportEntity = new TravelReportEntity("CityA", "CityB", LocalDate.now(),
                LocalTime.of(8, 0), LocalDate.now().plusDays(1), LocalTime.of(18, 0),
                approver, BigDecimal.ZERO, BigDecimal.ZERO);

        approvalEntity = new ApprovalEntity(travelReportEntity, approver, DEFAULT_ROLE);
    }

    @Test
    void should_initialize_with_pending_status() {
        // THEN
        assertThat(approvalEntity.getStatus()).isEqualTo(INITIAL_STATUS);
    }

    @Test
    void should_update_status_correctly() {
        // GIVEN
        ApprovalStatus newStatus = ApprovalStatus.APPROVED;

        // WHEN
        approvalEntity.updateApprovalStatus(newStatus);

        // THEN
        assertThat(approvalEntity.getStatus()).isEqualTo(newStatus);
    }

    @Test
    void should_validate_approval_status_when_status_is_pending() {
        // GIVEN
        approvalEntity.updateApprovalStatus(INITIAL_STATUS);

        // WHEN & THEN
        assertDoesNotThrow(() -> approvalEntity.checkIfStatusPending());
    }

    @Test
    void should_throw_approval_exception_when_status_is_not_pending() {
        // GIVEN
        approvalEntity.updateApprovalStatus(ApprovalStatus.APPROVED);

        // WHEN
        Executable executable = () -> approvalEntity.checkIfStatusPending();

        // THEN
        ApprovalException exc = assertThrows(ApprovalException.class, executable);
        assertThat(exc.getMessage()).contains("Approval already processed.");
    }

    @Test
    void equals_should_return_true_for_entities_with_same_travel_approver_and_role() {
        // GIVEN
        ApprovalEntity anotherApproval = new ApprovalEntity(travelReportEntity, approver, DEFAULT_ROLE);

        // THEN
        assertThat(approvalEntity).isEqualTo(anotherApproval);
    }

    @Test
    void equals_should_return_false_for_different_travel_entity() {
        // GIVEN
        TravelReportEntity differentTravel = new TravelReportEntity("CityX", "CityY",
                LocalDate.now(), LocalTime.of(9, 0),
                LocalDate.now().plusDays(2), LocalTime.of(19, 0),
                approver, BigDecimal.ZERO, BigDecimal.ZERO);

        ApprovalEntity differentApproval = new ApprovalEntity(differentTravel, approver, DEFAULT_ROLE);

        // THEN
        assertThat(approvalEntity).isNotEqualTo(differentApproval);
    }

    @Test
    void equals_should_return_false_for_different_approver() {
        // GIVEN
        UserEntity differentApprover = new UserEntity("different@test.com", "different", "approver", "password");
        differentApprover.changeRoleToAccountant();
        ApprovalEntity differentApproval = new ApprovalEntity(travelReportEntity, differentApprover, DEFAULT_ROLE);

        // THEN
        assertThat(approvalEntity).isNotEqualTo(differentApproval);
    }

    @Test
    void equals_should_return_false_for_different_role() {
        // GIVEN
        ApprovalEntity differentApproval = new ApprovalEntity(travelReportEntity, approver, Roles.ROLE_ACCOUNTANT);

        // THEN
        assertThat(approvalEntity).isNotEqualTo(differentApproval);
    }
}
