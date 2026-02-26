*Recipe Data Collection and API Development Assessment*

We are given a JSON file containing recipe data. 
Our task is to:
1. Parse the JSON data.
2. Store it in a database.
3. Develop a REST API to manage and query recipes.
   
*Database Schema for recipes table:*
```
● id: INT (Primary Key, Auto-increment) - Unique identifier for the recipe.
● title: VARCHAR - Name of the recipe
● cuisine: VARCHAR - Cuisine type
● rating: FLOAT - User rating of the recipe
● prep_time: INT - Preparation time in minutes
● cook_time: INT - Cooking time in minutes
● total_time: INT - Sum of prep_time and cook_time
● description: TEXT - Detailed description of the recipe
● nutrients: JSON - Nested nutritional information
● serves: VARCHAR - Number of servings
```

*API Development*
Developed a RESTful API to manage the recipe data, including endpoints for creating and
querying recipes.

a) POST /recipes
- Added a new recipe to the database.
- Automatically calculate total_time = prep_time + cook_time before saving.
- Return the complete saved recipe object, including the auto-generated id and calculated total_time.

  *Request body*
  ```
  {
  "id": 0,
  "title": "string",
  "cuisine": "string",
  "rating": 0.1,
  "prepTime": 20,
  "cookTime": 10,
  "description": "string",
  "nutrients": "string",
  "serves": "string"
  }
  ```
  *Response body*
  ```
  {
    "id": 8251,
    "title": "string",
    "cuisine": "string",
    "rating": 0.1,
    "prepTime": 20,
    "cookTime": 10,
    "totalTime": 30,
    "description": "string",
    "nutrients": "string",
    "serves": "string"
  }
  ```
b) GET /recipes/top
- Retrieved a list of the top-rated recipes.
- Queried the database and return recipes sorted by rating in descending order, restricted by the limit parameter.
- Returned a JSON object containing the list of filtered and sorted recipe objects under a data key.

*Request URL*
GET /recipes/top?limit=2
http://localhost:8080/recipes/top?page=0&limit=2
  
*Response Body*
  ```
  {
  "data": [
    {
      "id": 4608,
      "title": "Shrimp and Scallop Crepes",
      "cuisine": "Savory Crepes",
      "rating": 5,
      "prepTime": 20,
      "cookTime": 20,
      "totalTime": 70,
      "description": "Extra green onion and mushroom cheese sauce takes these shrimp and scallop crepes to whole new levels of savory awesomeness.",
      "nutrients": "{\"calories\":\"450 kcal\",\"carbohydrateContent\":\"29 g\",\"cholesterolContent\":\"216 mg\",\"fiberContent\":\"1 g\",\"proteinContent\":\"24 g\",\"saturatedFatContent\":\"15 g\",\"sodiumContent\":\"591 mg\",\"sugarContent\":\"7 g\",\"fatContent\":\"25 g\",\"unsaturatedFatContent\":\"0 g\"}",
      "serves": "6 servings"
    },
    {
      "id": 1537,
      "title": "Mel's Fabulously Easy Sauteed Okra",
      "cuisine": "Okra Recipes",
      "rating": 5,
      "prepTime": 10,
      "cookTime": 10,
      "totalTime": 20,
      "description": "This garlicky sauteed okra recipe is a 20-minute side dish that's perfect alongside any main course. The earthy taste of okra pairs nicely with the nutty flavor of Parmesan cheese.",
      "nutrients": "{\"calories\":\"152 kcal\",\"carbohydrateContent\":\"14 g\",\"cholesterolContent\":\"6 mg\",\"fiberContent\":\"4 g\",\"proteinContent\":\"6 g\",\"saturatedFatContent\":\"2 g\",\"sodiumContent\":\"118 mg\",\"sugarContent\":\"2 g\",\"fatContent\":\"9 g\",\"unsaturatedFatContent\":\"0 g\"}",
      "serves": "4 servings"
    }
  ],
  "page number": 0,
  "total elements": 8251,
  "total pages": 4126,
  "page size": 2
}
```

