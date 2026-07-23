package com.securin.recipe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securin.recipe.model.Recipe;
import com.securin.recipe.model.RecipeRequest;
import com.securin.recipe.repository.RecipeRepository;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    private final ObjectMapper objectMapper;

    RecipeService(RecipeRepository recipeRepository, ObjectMapper objectMapper) {
        this.recipeRepository = recipeRepository;
        this.objectMapper = objectMapper;
    }
    public Recipe createRecipe(RecipeRequest request) {

        Recipe recipe = new Recipe();

        recipe.setTitle(request.getTitle());
        recipe.setCuisine(request.getCuisine());
        recipe.setPrepTime(request.getPrepTime());
        recipe.setCookTime(request.getCookTime());
        recipe.setTotalTime(request.getTotalTime());
        recipe.setDescription(request.getDescription());
        recipe.setServes(request.getServes());

        try {
            recipe.setNutrients(
                    objectMapper.writeValueAsString(request.getNutrients()));
        } catch (Exception e) {
            throw new RuntimeException("Error converting nutrients");
        }

        return recipeRepository.save(recipe);
    }
    public Page<Recipe> getAllRecipes(Pageable pageable){
        return recipeRepository.findAll(pageable);
    }

}
