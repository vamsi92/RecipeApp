package com.strikerapps.recipeapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetRecipeDetails {

    private Constants myConst;
    private static GetRecipeDetails instance=null;

    private GetRecipeDetails(){
        Retrofit retrofit=new Retrofit.Builder().baseUrl(Constants.RECIPE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        myConst=retrofit.create(Constants.class);

    }
    public static synchronized GetRecipeDetails getInstance(){
        if(instance==null){
            instance=new GetRecipeDetails();
        }
        return instance;
    }

    public Constants getMyConst(){
        return myConst;
    }
}
