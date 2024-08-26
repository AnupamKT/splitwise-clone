package com.anupam.Splitwise.repository.group;

import com.anupam.Splitwise.entity.group.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, UUID> {

    Optional<GroupEntity>findByGroupName(String groupName);
    Optional<GroupEntity>findByGroupId(UUID groupId);
}
