package com.capstone.Recipe.controller;

import com.capstone.Recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.Map;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/meal-plan")
    public String showMealPlanForm() {
        return "recipe_meal_plan"; // 식단 생성 폼을 나타내는 HTML 템플릿의 이름을 반환합니다.
    }

    @GetMapping("/generate-meal-plan")
    @ResponseBody
    public Map<String, Object> generateMealPlan(@RequestParam(required = false) String date,
                                                @RequestParam(required = false, defaultValue = "0") int div,
                                                @RequestParam(required = false, defaultValue = "1") int serving) {
        LocalDate selectedDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        return recipeService.recommendMonthlyMealPlan(selectedDate, div, serving);
    }
}

