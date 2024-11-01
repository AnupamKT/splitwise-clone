package com.anupam.Splitwise.service.audit;

import com.anupam.Splitwise.common.SplitwiseConstants;
import com.anupam.Splitwise.converter.audit.AuditConverter;
import com.anupam.Splitwise.entity.audit.AuditEntity;
import com.anupam.Splitwise.exception.audit.AuditException;
import com.anupam.Splitwise.exception.audit.InvalidAuditException;
import com.anupam.Splitwise.handler.audit.AuditHandler;
import com.anupam.Splitwise.model.Response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;


@Service
@Slf4j
public class AuditService {

    @Autowired
    private AuditHandler auditHandler;
    @Autowired
    private AuditConverter auditConverter;

    //Queue to hold 10 audit entries,used ArrayBlockingQueue for multiple threads
    private Queue<AuditEntity> auditEntityQueue = new ArrayBlockingQueue<>(10);


    public void submitAudit(Object obj, SplitwiseConstants.AuditAction auditAction, String email) {
        CompletableFuture.runAsync(() -> processAudit(obj, auditAction, email)).
                thenRun(() -> log.debug("Audit message for successfully submitted for action:{}", auditAction.name())).
                exceptionally(ex -> {
                    log.error("Audit message submit failed for action:{} with error message:{}", auditAction.name(), ex.getMessage());
                    return null;
                });

    }

    private void processAudit(Object obj, SplitwiseConstants.AuditAction auditAction, String email) {
        try {
            AuditEntity auditEntity = auditConverter.convertToAuditEntity(obj, auditAction, email);
            addAuditToQueue(auditEntity);
        } catch (InvalidAuditException ex) {
            log.error("error occurred while processing audit message:{}", ex.getMessage());
        }
    }

    private void addAuditToQueue(AuditEntity auditEntity) {
        boolean isSubmitted = auditEntityQueue.offer(auditEntity);
        if (!isSubmitted) {
            log.debug("AuditService::Triggering persist of Audit messages in DB");
            triggerPersistInDB();
            auditEntityQueue.offer(auditEntity);
        }
    }

    public void triggerPersistInDB() {
        log.debug("AuditService:: start of persisting audit messages in DB");
        if (!CollectionUtils.isEmpty(auditEntityQueue)) {
            auditHandler.persistAuditMessages(auditEntityQueue);
            auditEntityQueue.clear();
        }
        log.debug("AuditService:: End of persisting audit messages in DB");
    }

    /**
     * This method fetches all activity(audit messages) for the given emailId
     */
    public Response getActivityForEmail(String email) throws AuditException {
        List<AuditEntity> auditEntityList = new ArrayList<>();
        try {
            auditEntityList = auditHandler.getAuditMessagesForEmail(email);
        } catch (Exception ex) {
            String msg = "error occurred while fetching activities for user with email:"
                    + email + " with error message:" + ex.getMessage();
            log.error(msg);
            throw new AuditException(msg);
        }
        return Response.builder().
                status(HttpStatus.OK.value()).
                body(auditEntityList).build();
    }
}

