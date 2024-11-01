package com.anupam.Splitwise.converter.audit;

import com.anupam.Splitwise.common.SplitwiseConstants;
import com.anupam.Splitwise.entity.audit.AuditEntity;
import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.exception.audit.InvalidAuditException;
import com.anupam.Splitwise.model.expense.GroupExpense;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuditConverter {

    public AuditEntity convertToAuditEntity(Object obj, SplitwiseConstants.AuditAction auditAction, String email) throws InvalidAuditException {
        AuditEntity auditEntity = null;
        switch (auditAction) {
            case ADD_GROUP:
                auditEntity = createAddGroupAuditEntity(obj);
                break;
            case DELETE_GROUP:
                auditEntity = createDeleteGroupAuditEntity(obj);
                break;
            case ADD_MEMBER:
                auditEntity = createAddMemberAuditEntity(obj, email);
                break;
            case REMOVE_MEMBER:
                auditEntity = createRemoveMemberAuditEntity(obj, email);
                break;
            case ADD_EXPENSE:
                auditEntity = createAddExpenseAuditEntity(obj);
                break;
            default:
                throw new InvalidAuditException("invalid audit action:" + auditAction.name());
        }
        return auditEntity;
    }

    private AuditEntity createAddExpenseAuditEntity(Object obj) throws InvalidAuditException {
        if (obj instanceof GroupExpense) {
            GroupExpense groupExpense = (GroupExpense) obj;
            GroupEntity groupEntity = groupExpense.getGroup();
            AuditEntity auditEntity = new AuditEntity();
            auditEntity.setGroup(groupEntity);
            auditEntity.setCreatedDate(new Date());
            String activityName = groupExpense.getPaidBy() + " added "
                    + groupExpense.getAmount() + " to the group "
                    + groupEntity.getGroupName();
            auditEntity.setActivityName(activityName);
            return auditEntity;
        } else {
            throw new InvalidAuditException("invalid audit object for add expense action");
        }
    }

    private AuditEntity createRemoveMemberAuditEntity(Object obj, String email) throws InvalidAuditException {
        if (obj instanceof GroupEntity) {
            GroupEntity groupEntity = (GroupEntity) obj;
            AuditEntity auditEntity = new AuditEntity();
            auditEntity.setGroup(groupEntity);
            String activityName = email + " removed from the group " + groupEntity.getGroupName();
            auditEntity.setActivityName(activityName);
            auditEntity.setCreatedDate(new Date());
            return auditEntity;
        } else {
            throw new InvalidAuditException("invalid audit object for remove member action");
        }
    }

    private AuditEntity createAddMemberAuditEntity(Object obj, String email) throws InvalidAuditException {
        if (obj instanceof GroupEntity) {
            GroupEntity groupEntity = (GroupEntity) obj;
            AuditEntity auditEntity = new AuditEntity();
            auditEntity.setGroup(groupEntity);
            String activityName = email + " added to the group " + groupEntity.getGroupName();
            auditEntity.setActivityName(activityName);
            auditEntity.setCreatedDate(new Date());
            return auditEntity;
        } else {
            throw new InvalidAuditException("invalid audit object for add member action");
        }
    }

    private AuditEntity createDeleteGroupAuditEntity(Object obj) throws InvalidAuditException {
        if (obj instanceof GroupEntity) {
            GroupEntity groupEntity = (GroupEntity) obj;
            AuditEntity auditEntity = new AuditEntity();
            auditEntity.setGroup(groupEntity);
            String activityName = groupEntity.getGroupAdmin() + " deleted the group " + groupEntity.getGroupName();
            auditEntity.setActivityName(activityName);
            auditEntity.setCreatedDate(new Date());
            return auditEntity;
        } else {
            throw new InvalidAuditException("invalid audit object for delete group action");
        }
    }

    private AuditEntity createAddGroupAuditEntity(Object obj) throws InvalidAuditException {
        if (obj instanceof GroupEntity) {
            GroupEntity groupEntity = (GroupEntity) obj;
            AuditEntity auditEntity = new AuditEntity();
            auditEntity.setGroup(groupEntity);
            String activityName = groupEntity.getGroupAdmin() + " created the group " + groupEntity.getGroupName();
            auditEntity.setActivityName(activityName);
            auditEntity.setCreatedDate(new Date());
            return auditEntity;
        } else {
            throw new InvalidAuditException("invalid audit object for add group action");
        }
    }
}
