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
package pl.sginko.travelexpense.domain.actionLog.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.sginko.travelexpense.domain.actionLog.entity.ActionLogEntity;
import pl.sginko.travelexpense.domain.actionLog.repository.ActionLogRepository;

@AllArgsConstructor
@Service
public class ActionLogServiceImpl implements ActionLogService {
    private final ActionLogRepository actionLogRepository;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(String message, Long travelId, Long approverId) {
        ActionLogEntity log = new ActionLogEntity(message, travelId, approverId, System.currentTimeMillis());

        actionLogRepository.save(log);
    }
}
