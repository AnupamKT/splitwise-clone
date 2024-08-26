package com.anupam.Splitwise.handler.settlements;

import com.anupam.Splitwise.entity.expense.SettlementsEntity;
import com.anupam.Splitwise.repository.settlement.SettlementRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
public class SettlementHandler {

    @Autowired
    private SettlementRepository repository;

    @Transactional
    public List<SettlementsEntity> addSettlements(Set<SettlementsEntity> settlementsEntities, UUID groupId) throws Exception {
        log.info("started addSettlements for groupId:{}", groupId);
        long startTime = Instant.now().toEpochMilli();
        try {
            return repository.saveAll(settlementsEntities);
        } catch (Exception ex) {
            log.error("error occurred while adding settlements for group:{} with error message:{}", groupId, ex.getMessage());
            throw ex;
        } finally {
            long endTime = Instant.now().toEpochMilli();
            Long totalTime = endTime - startTime;
            log.info("ended addSettlements for group:{}, total time taken is:{}", groupId, totalTime);
        }
    }

    @Transactional
    public void deleteSettlements(UUID groupId) throws Exception {
        log.info("started deleteSettlements for groupId:{}", groupId);
        long startTime = Instant.now().toEpochMilli();
        try {
            repository.deleteAllByGroupId(groupId);
        } catch (Exception ex) {
            log.error("error occurred while deleting settlements for groupId:{} with error message:{}", groupId, ex.getMessage());
            throw ex;
        } finally {
            long endTime = Instant.now().toEpochMilli();
            Long totalTime = endTime - startTime;
            log.info("ended deleteSettlements for groupId:{}, total time taken is:{}", groupId, totalTime);
        }
    }
}
