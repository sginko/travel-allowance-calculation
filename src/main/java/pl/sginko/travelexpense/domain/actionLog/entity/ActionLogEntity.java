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
package pl.sginko.travelexpense.domain.actionLog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "action_log")
public class ActionLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private Long travelReportId;
    private Long approverId;
    private long timestamp;

    public ActionLogEntity(String message, Long travelReportId, Long approverId, long timestamp) {
        this.message = message;
        this.travelReportId = travelReportId;
        this.approverId = approverId;
        this.timestamp = timestamp;
    }

    public void updateMessage(String message) {
        this.message = message;
    }

    public void updateTravelId(Long travelReportId) {
        this.travelReportId = travelReportId;
    }

    public void updateApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public void updateTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
