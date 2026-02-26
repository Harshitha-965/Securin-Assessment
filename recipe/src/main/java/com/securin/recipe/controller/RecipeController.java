package com.securin.recipe.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.securin.recipe.model.Recipe;
import com.securin.recipe.service.RecipeService;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    public Map<String,Object> buildPageResponse(Page<Recipe> page){
        Map<String,Object> response=new LinkedHashMap<>();
        response.put("data",page.getContent());
        response.put("page number",page.getNumber());
        response.put("total elements",page.getTotalElements());
        response.put("total pages",page.getTotalPages());
        response.put("page size",page.getSize());
        return response;
    }
    public Pageable buildPageable(int page,int limit){
        return PageRequest.of(page, limit,Sort.by("rating").descending());
    }

    @PostMapping
    public Recipe createRecipes(@RequestBody Recipe r){
        Integer totalTime=r.getPrepTime()+r.getCookTime();
        r.setTotalTime(totalTime);
        return recipeService.createRecipe(r);
    }
    @GetMapping("/top")
    public ResponseEntity<?> getAllRecipes(
        @RequestParam(defaultValue="0")int page,
        @RequestParam(defaultValue="5")int limit
    ){
        Pageable pageable=buildPageable(page,limit);
        Page<Recipe> result=recipeService.getAllRecipes(pageable);
        if(result.isEmpty()){
            Map<String,String> error=new HashMap<>();
            error.put("message","No products found in the Database");
            return ResponseEntity.status(404).body(result);
        }
        return ResponseEntity.ok(buildPageResponse(result));
    }

}
