package com.capstone.Recipe.controller;

import com.capstone.Recipe.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/meal-plan")
    public String showMealPlanForm() {
        return "recipe_meal_plan"; //
    }

    @GetMapping("/generate-meal-plan")
    @ResponseBody
    public List<List<List<String>>> generateMealPlan(@RequestParam(required = false) String date,
                                                     @RequestParam(required = false, defaultValue = "0") int div) {
        LocalDate selectedDate = (date != null) ? LocalDate.parse(date) : LocalDate.now();
        return recipeService.recommendMonthlyMealPlan(selectedDate, div);
    }
}
