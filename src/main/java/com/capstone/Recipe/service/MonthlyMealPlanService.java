package com.capstone.Recipe.service;

import com.capstone.Recipe.entity.MonthlyMealPlanEntity;
import com.capstone.Recipe.repository.MonthlyMealPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonthlyMealPlanService {

    @Autowired
    private MonthlyMealPlanRepository monthlyMealPlanRepository;

    public void saveMonthlyMealPlan(MonthlyMealPlanEntity monthlyMealPlan) {
        // ID 생성
        monthlyMealPlan.generateId();

        // 각 항목에 ID 할당
        monthlyMealPlan.assignIdToMealPlan();

        // 저장
        monthlyMealPlanRepository.save(monthlyMealPlan);
    }
}
