package com.anupam.Splitwise.handler.audit;

import com.anupam.Splitwise.entity.audit.AuditEntity;
import com.anupam.Splitwise.repository.audit.AuditRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Queue;

@Component
@Slf4j
public class AuditHandler {
    @Autowired
    private AuditRepository auditRepository;

    public void persistAuditMessages(Queue<AuditEntity> auditEntities) {
        long startTime = Instant.now().toEpochMilli();
        log.info("started submitting audit messages");
        try {
            auditRepository.saveAllAndFlush(auditEntities);
        } catch (Exception ex) {
            log.error("submitting audit messages failed with error messages:{}", ex.getMessage());
        } finally {
            long endTime = Instant.now().toEpochMilli();
            long totalTime = endTime - startTime;
            log.debug("Ended submit of audit messages, total time taken is:{}", totalTime);
        }
    }

    public List<AuditEntity> getAuditMessagesForEmail(String email) throws Exception {
        long startTime = Instant.now().toEpochMilli();
        log.info("start of getAuditMessagesForEmail for email:{}", email);
        try {
            return auditRepository.findAllAuditMessagesByUserEmail(email);
        } catch (Exception ex) {
            log.error("error occurred while fetching audit messages for user with email:{}, error message is:{}", email, ex.getMessage());
            throw ex;
        } finally {
            long endTime = Instant.now().toEpochMilli();
            long totalTime = endTime - startTime;
            log.info("end of getAuditMessagesForEmail for email:{},total time taken is:{}", email, totalTime);
        }
    }
}
