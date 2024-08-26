package com.anupam.Splitwise.threads;

import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.anupam.Splitwise.entity.expense.SettlementsEntity;
import com.anupam.Splitwise.entity.group.GroupEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class ExpenseSettlement {
    private GroupEntity groupEntity;

    public ExpenseSettlement(GroupEntity groupEntity) {
        this.groupEntity = groupEntity;
    }

    public Set<SettlementsEntity> settle() {
        int totalExpense = calculateTotalExpense(groupEntity);
        int totalMembers = groupEntity.getUserEntities().size();
        double perHeadShare = totalExpense / totalMembers;
        //This map contains sum of expenses by all members
        Map<String, Integer> perPersonExpenseMap = calculatePerPersonExpense(groupEntity);

        //This map contains list of user and diff from perHeadShare
        Map<String, Double> settlementMap = new HashMap<>();
        for (Map.Entry<String, Integer> entry : perPersonExpenseMap.entrySet()) {

            //if this value is positive, person will get
            //if this value is negative, person will give
            double settlements = entry.getValue() - perHeadShare;
            settlementMap.put(entry.getKey(), settlements);
        }
        //using predicates to avoid duplicates
        Predicate<Map.Entry<String, Double>> giversPredicate = entry -> entry.getValue() < 0;
        Predicate<Map.Entry<String, Double>> takersPredicate = entry -> entry.getValue() > 0;
        //givers list
        List<String> givers = findGiversAndTakers(settlementMap, giversPredicate);
        //takers list--those who will get money
        List<String> takers = findGiversAndTakers(settlementMap, takersPredicate);
        //List of settlement comments
        List<String> settlementsComments = createSettlementsComments(givers, takers, settlementMap);
        Set<SettlementsEntity> settlementsEntities = convertSettlementsData(settlementsComments);
        return settlementsEntities;
    }

    private Set<SettlementsEntity> convertSettlementsData(List<String> settlementsComments) {
        Set<SettlementsEntity> settlementsEntitySet = new HashSet<>();
        settlementsComments.stream().forEach(comments -> {
            SettlementsEntity settlementsEntity = SettlementsEntity.
                    builder().
                    settlementAction(comments).
                    createdDate(new Date()).
                    group(this.groupEntity).build();
            settlementsEntitySet.add(settlementsEntity);
        });
        return settlementsEntitySet;
    }

    private List<String> findGiversAndTakers(Map<String, Double> settlementMap, Predicate<Map.Entry<String, Double>> predicate) {
        return settlementMap.
                entrySet().
                stream().
                filter(predicate).
                map(entry -> entry.getKey()).
                collect(Collectors.toList());
    }

    private List<String> createSettlementsComments(List<String> givers, List<String> takers, Map<String, Double> settlementMap) {
        List<String> settlementsComments = new ArrayList<>();
        while (takers.size() != 0) {
            String giver = givers.get(0);
            Double giverValue = settlementMap.get(giver) * (-1);
            String taker = takers.get(0);
            Double takerValue = settlementMap.get(taker);
            Double diff = takerValue - giverValue;
            if (diff == 0) {
                String comment = taker + " will get " + takerValue + " from " + giver;
                settlementsComments.add(comment);
                givers.remove(0);
                takers.remove(0);
            } else if (diff > 0) {
                String comment = taker + " will get " + giverValue + " from " + giver;
                settlementsComments.add(comment);
                givers.remove(0);
                settlementMap.put(taker, diff);
            } else if (diff < 0) {
                String comment = taker + " will get " + takerValue + " from " + giver;
                settlementsComments.add(comment);
                takers.remove(0);
                settlementMap.put(giver, diff);
            }
        }
        return settlementsComments;
    }

    private Map<String, Integer> calculatePerPersonExpense(GroupEntity groupEntity) {
        Map<String, Integer> perPersonExpense;
        List<String> emailList = groupEntity.
                getUserEntities().
                stream().
                map(userEntity -> userEntity.getEmail()).
                collect(Collectors.toList());

        Set<GroupExpenseEntity> expenseEntities = groupEntity.getExpenses();

        perPersonExpense = emailList.stream()
                .collect(Collectors.toMap(
                        email -> email,
                        email -> expenseEntities.stream()
                                .filter(expenseEntity -> expenseEntity.getPaidBy().equalsIgnoreCase(email))
                                .mapToInt(expenseEntity -> expenseEntity.getAmount())
                                .sum()
                ));
        log.debug("Per per expense for group:{} is:{}", this.groupEntity.getGroupName(), perPersonExpense);
        return perPersonExpense;
    }

    private int calculateTotalExpense(GroupEntity groupEntity) {
        int totalExpense = groupEntity.
                getExpenses().
                stream().
                mapToInt(expenseEntity -> expenseEntity.getAmount()).
                sum();
        log.debug("total expense for group:{} is:{}", this.groupEntity.getGroupName(), totalExpense);
        return totalExpense;
    }
}
