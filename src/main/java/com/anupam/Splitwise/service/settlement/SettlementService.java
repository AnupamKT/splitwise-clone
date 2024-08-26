package com.anupam.Splitwise.service.settlement;

import com.anupam.Splitwise.entity.expense.SettlementsEntity;
import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.handler.group.GroupHandler;
import com.anupam.Splitwise.handler.settlements.SettlementHandler;
import com.anupam.Splitwise.threads.ExpenseSettlement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class SettlementService {
    @Autowired
    private GroupHandler groupHandler;
    @Autowired
    private SettlementHandler settlementHandler;
    @Autowired
    private ExecutorService executorService;

    public void calculateSettlementsAsync(UUID groupId) {
        CompletableFuture<Set<SettlementsEntity>> settlementsFuture = CompletableFuture.
                supplyAsync(() -> calculateSettlements(groupId), executorService);
        settlementsFuture.thenAccept(result -> persistSettlementsData(result, groupId));
        settlementsFuture.join();
        log.info("settlement rebalanced for group:{}",groupId);
    }

    @Transactional
    public Set<SettlementsEntity> calculateSettlements(UUID groupId) {
        try {
            GroupEntity groupEntity = groupHandler.findGroupById(groupId);
            ExpenseSettlement settlement = new ExpenseSettlement(groupEntity);
            Set<SettlementsEntity> settlementsEntities = settlement.settle();
            return settlementsEntities;
        } catch (Exception ex) {
            log.error("error occurred while calculateSettlements for groupId:{} with error message:{}", groupId, ex.getMessage());
        }
        return null;
    }

    @Transactional
    private void persistSettlementsData(Set<SettlementsEntity> settlementsEntities, UUID groupId) {
        try {
            //first delete all settlements for group and then add the settlements
            settlementHandler.deleteSettlements(groupId);
            settlementHandler.addSettlements(settlementsEntities, groupId);
        } catch (Exception e) {
            log.error("error saving settlement data for group:{} with error message:{}", groupId, e.getMessage());
        }
    }
}
