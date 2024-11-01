package com.anupam.Splitwise.repository.audit;

import com.anupam.Splitwise.entity.audit.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditRepository  extends JpaRepository<AuditEntity,UUID > {

    // Custom query to fetch all audit messages for groups associated with a user's email
    @Query("SELECT a FROM AuditEntity a JOIN a.group g JOIN g.userEntities u WHERE u.email = :email")
    List<AuditEntity> findAllAuditMessagesByUserEmail(@Param("email") String email);
}
