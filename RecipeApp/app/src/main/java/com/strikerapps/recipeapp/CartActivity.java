package com.strikerapps.recipeapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity {
    ArrayList<Recipes> shopCart=new ArrayList<Recipes>();
    HashMap<String,Integer> quantityMap=new HashMap<>();
    private Activity activity;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        // Set Back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Cart");
        activity=CartActivity.this;
        TextView noCartText=(TextView)findViewById(R.id.noCartView);
        shopCart=(ArrayList<Recipes>) getIntent().getExtras().getSerializable("cartItems");

        if(shopCart.size()>0) {
            quantityMap = getQuantityDetails();

            ListView listView = (ListView) findViewById(R.id.list_cart);
            CustomMapAdapter adapter = new CustomMapAdapter(quantityMap, shopCart, this.getBaseContext());

            listView.setAdapter(adapter);
        }
        else{


            noCartText.setText("No items in the cart");
        }
    }
    HashMap<String,Integer> getQuantityDetails(){
        HashMap<String,Integer> hmap=new HashMap<String, Integer>();
        for(int i=0;i<shopCart.size();i++){
            if(!hmap.containsKey(shopCart.get(i).getName())){
                hmap.put(shopCart.get(i).getName(),1);
            }else{
                hmap.put(shopCart.get(i).getName(),hmap.get(shopCart.get(i).getName())+1);
            }
        }

        return hmap;
    }

    // MENU BUTTONS ------------------------------------------------------------------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            // DEFAULT BACK BUTTON
            case android.R.id.home:
                this.finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
