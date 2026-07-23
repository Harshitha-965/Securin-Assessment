package com.securin.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.securin.recipe.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe,Integer> {

    
}
