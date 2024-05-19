package com.capstone.Recipe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
public class MonthlyMealPlanEntity {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private int servings;
    private String totalPrice;

    @ElementCollection
    @CollectionTable(name = "meal_plan")
    private List<String> mealPlan;

    // 생성자, getter, setter 등 필요한 메서드 추가

    // ID 생성 메서드
    public void generateId() {
        LocalDate currentDate = LocalDate.now();
        String idStr = String.format("%d%02d%02d", currentDate.getYear(), currentDate.getMonthValue(), currentDate.getDayOfMonth());
        this.id = Long.parseLong(idStr);
    }

    // 식단 항목에 ID 할당
    public void assignIdToMealPlan() {
        if (mealPlan != null) {
            for (String item : mealPlan) {
                item += " (ID: " + this.id + ")";
            }
        }
    }
}
