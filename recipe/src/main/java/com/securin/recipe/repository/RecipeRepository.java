package com.securin.recipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.securin.recipe.model.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe,Integer> {
    
}
