package com.anupam.Splitwise.repository.settlement;

import com.anupam.Splitwise.entity.expense.SettlementsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SettlementRepository extends JpaRepository<SettlementsEntity, UUID> {

    @Query(nativeQuery = true,value = "delete from settlement_details where group_id=:groupId")
    @Modifying
    void deleteAllByGroupId(@Param("groupId") UUID groupId);
}
