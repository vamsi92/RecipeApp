package com.strikerapps.recipeapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface  Constants {
    String RECIPE_URL="https://s3-ap-southeast-1.amazonaws.com/he-public-data/";

    @GET("reciped9d7b8c.json")
    Call<List<Recipes>> getRecipes();
}
