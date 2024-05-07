package com.capstone.Recipe.service;

import com.capstone.Recipe.entity.RecipeEntity;
import com.capstone.Recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    //식단을 추천해 주는 함수
    public List<List<List<String>>> recommendMonthlyMealPlan(LocalDate selectedDate, int div) {
        List<List<List<String>>> monthlyMealPlan = new ArrayList<>(); // 식단을 담을 리스트
        int daysInMonth = selectedDate.lengthOfMonth(); // 선택한 날짜의 해당 월의 일 수 가져오기

        for (int i = 1; i <= daysInMonth; i++) {
            List<RecipeEntity> dailyRecipes = getRandomRecipesByCategory(); // 하루에 6가지 추천
            while (!validateNutritionalComposition(dailyRecipes, div)) {
                dailyRecipes = getRandomRecipesByCategory(); // 영양소 조건을 만족하지 못하면 다시 추천
            }
            List<List<String>> dailyMealPlan = convertToTitleListWithNutritionalInfo(dailyRecipes);
            dailyMealPlan.add(0, Collections.singletonList("날짜: " + selectedDate.withDayOfMonth(i))); // 날짜 정보 추가
            monthlyMealPlan.add(dailyMealPlan);
        }

        return monthlyMealPlan;
    }

    // 각 카테고리에서 랜덤한 레시피를 선택하는 함수
    private List<RecipeEntity> getRandomRecipesByCategory() {
        List<RecipeEntity> allRecipes = recipeRepository.findAll();
        Map<String, RecipeEntity> categoryMap = new LinkedHashMap<>(); //식단을 저장할 맵

        // 각 카테고리에서 랜덤하게 레시피 선택
        categoryMap.put("밥", getRandomRecipeByCategory(allRecipes, "밥"));
        categoryMap.put("국&찌개", getRandomRecipeByCategory(allRecipes, "국&찌개"));
        categoryMap.put("일품", getRandomRecipeByCategory(allRecipes, "일품"));
        categoryMap.put("반찬", getRandomRecipeByCategory(allRecipes, "반찬"));
        categoryMap.put("나물/샐러드", getRandomRecipeByCategory(allRecipes, "나물/샐러드"));
        categoryMap.put("김치", getRandomRecipeByCategory(allRecipes, "김치"));

        return new ArrayList<>(categoryMap.values());
    }

    //카테고리 내에서 메뉴를 추천해 줄 함수
    private RecipeEntity getRandomRecipeByCategory(List<RecipeEntity> recipes, String category) {
        List<RecipeEntity> categoryRecipes = new ArrayList<>();
        for (RecipeEntity recipe : recipes) {
            if (recipe.getCategory().equals(category)) {
                categoryRecipes.add(recipe);
            }
        }
        if (categoryRecipes.isEmpty()) {
            return null;
        }
        return categoryRecipes.get(new Random().nextInt(categoryRecipes.size()));
    }

    //해당 식단의 영양성분이 기준에 충족되는지 확인하는 함수
    private boolean validateNutritionalComposition(List<RecipeEntity> recipes, int div) {
        double totalCalories = 0;
        double totalCarbohydrates = 0;
        double totalProteins = 0;
        double totalFats = 0;

        for (RecipeEntity recipe : recipes) { // 각 메뉴별 칼로리, 탄수화물, 단백질, 지방 값
            totalCalories += Double.parseDouble(recipe.getEng());
            totalCarbohydrates += Double.parseDouble(recipe.getCar());
            totalProteins += Double.parseDouble(recipe.getPro());
            totalFats += Double.parseDouble(recipe.getFat());
        }

        // 열량 조건 검사
        if(div == 0){ //일반적인 성인 기준
            if (totalCalories <= 650 || totalCalories >= 750) {
                return false;
            }
        }
        else{ // 중/고등학교 학생 기준
            if (totalCalories <= 850 || totalCalories >= 900) {
                return false;
            }
        }

        // 탄수화물 조건 검사 (전체 칼로리의 65% 이하)
        double carbPercentage = (totalCarbohydrates / totalCalories) * 100;
        if (carbPercentage >= 65) {
            return false;
        }

        // 단백질 조건 검사 (전체 칼로리의 20% 이하)
        double proteinPercentage = (totalProteins / totalCalories) * 100;
        if (proteinPercentage >= 20) {
            return false;
        }

        // 지방 조건 검사 (전체 칼로리의 30% 이하)
        double fatPercentage = (totalFats / totalCalories) * 100;
        if (fatPercentage >= 30) {
            return false;
        }

        return true;
    }

    //출력 형식이 제목 + 영양성분만 출력되게 수정해주는 함수
    private List<List<String>> convertToTitleListWithNutritionalInfo(List<RecipeEntity> recipes) {
        List<List<String>> mealPlan = new ArrayList<>();
        double totalCalories = 0;
        double totalCarbohydrates = 0;
        double totalProteins = 0;
        double totalFats = 0;

        for (RecipeEntity recipe : recipes) {
            totalCalories += Double.parseDouble(recipe.getEng());
            totalCarbohydrates += Double.parseDouble(recipe.getCar());
            totalProteins += Double.parseDouble(recipe.getPro());
            totalFats += Double.parseDouble(recipe.getFat());

            mealPlan.add(Collections.singletonList(formatRecipeWithNutritionalInfo(recipe)));
        }

        mealPlan.add(Collections.singletonList(formatNutritionalInfo(totalCalories, totalCarbohydrates, totalProteins, totalFats)));

        return mealPlan;
    }
    //각 메뉴별 메뉴명 및 영양성분 출력함수
    private String formatRecipeWithNutritionalInfo(RecipeEntity recipe) {
        return String.format("%s: %s (열량: %.1fkcal, 탄수화물: %.1fg, 단백질: %.1fg, 지방: %.1fg)",
                recipe.getCategory(), recipe.getTitle(), Double.parseDouble(recipe.getEng()),
                Double.parseDouble(recipe.getCar()), Double.parseDouble(recipe.getPro()), Double.parseDouble(recipe.getFat()));
    }

    //식단의 영양성분 출력함수
    private String formatNutritionalInfo(double totalCalories, double totalCarbohydrates, double totalProteins, double totalFats) {
        return String.format("총 열량: %.1fkcal, 탄수화물: %.1fg, 단백질: %.1fg, 지방: %.1fg",
                totalCalories, totalCarbohydrates, totalProteins, totalFats);
    }
}
