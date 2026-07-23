package com.securin.recipe.model;
import com.fasterxml.jackson.annotation.JsonProperty;
public class RecipeRequest {

    private String title;
    private String cuisine;
    @JsonProperty("prep_time")
    private Integer prepTime;
    @JsonProperty("cook_time")
    private Integer cookTime;
    private Integer totalTime;
    private String description;
    private Nutrients nutrients;
    private String serves;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCuisine() {
        return cuisine;
    }
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }
    public Integer getPrepTime() {
        return prepTime;
    }
    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }
    public Integer getCookTime() {
        return cookTime;
    }
    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }
    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }
    public Integer getTotalTime() {
        return totalTime;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Nutrients getNutrients() {
        return nutrients;
    }
    public void setNutrients(Nutrients nutrients) {
        this.nutrients = nutrients;
    }
    public String getServes() {
        return serves;
    }
    public void setServes(String serves) {
        this.serves = serves;
    }

    
}
