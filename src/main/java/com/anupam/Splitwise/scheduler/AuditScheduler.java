package com.anupam.Splitwise.scheduler;

import com.anupam.Splitwise.service.audit.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@EnableScheduling
@Component
@Slf4j
public class AuditScheduler {

    @Autowired
    private AuditService auditService;

    @Scheduled(fixedDelayString = "${splitwise.audit.scheduler.persist.time.hour}")
    public void persistAuditMessages() {
        log.debug("started scheduler persistAuditMessages at:{}", Instant.now());
        auditService.triggerPersistInDB();
        log.debug("Ended scheduler persistAuditMessages at:{}", Instant.now());
    }
}
