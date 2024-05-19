package com.capstone.Recipe.repository;

import com.capstone.Recipe.entity.MonthlyMealPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyMealPlanRepository extends JpaRepository<MonthlyMealPlanEntity, Long> {
}
