package com.strikerapps.recipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    CardView cardView;
    private static CustomAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static List<Recipes> data;
    static View.OnClickListener myOnClickListener;
    SwipeController swipeController=null;
    ArrayList<Recipes> newList=new ArrayList<Recipes>();
    ArrayList<Recipes> cartItems= new ArrayList<Recipes>();
    ArrayList<Recipes> finalCartItems= new ArrayList<Recipes>();
    TextView cartView;
    private DBManager dbManager;
    public int cartSize=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardView =findViewById(R.id.card_view);
        recyclerView = (RecyclerView) findViewById(R.id.recy_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getRecipesFromApi();
        adapter = new CustomAdapter(newList, MainActivity.this);
        recyclerView.setAdapter(adapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                Recipes obj=newList.get(position);
                newList.remove(position);
                newList.add(obj);
                adapter = new CustomAdapter(newList, MainActivity.this);
                recyclerView.setAdapter(adapter);

                //adapter.notifyItemRemoved(position);
                //adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }

            @Override
            public void onLeftClicked(int position) {

                this.cartList.add(adapter.getData().get(position));
                cartItems=cartList;
                updateCart();
            }

        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });


    }


    public void getRecipesFromApi(){
        ArrayList<Recipes> recipesList=new ArrayList<>();
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        if(cursor.getCount()==0) {
            Call<List<Recipes>> call = GetRecipeDetails.getInstance().getMyConst().getRecipes();
            call.enqueue(new Callback<List<Recipes>>() {
                @Override
                public void onResponse(Call<List<Recipes>> call, Response<List<Recipes>> response) {
                    data = response.body();
                    for (int i = 0; i < data.size(); i++) {
                        dbManager.insert(data.get(i).getName(), data.get(i).getImage(), data.get(i).getCategory(), data.get(i).getLabel(), data.get(i).getPrice(), data.get(i).getDescription());
                        recipesList.add(new Recipes(data.get(i).getId(), data.get(i).getName(), data.get(i).getImage(), data.get(i).getCategory(), data.get(i).getLabel(), data.get(i).getPrice(), data.get(i).getDescription()));
                    }
                    adapter = new CustomAdapter(recipesList, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<Recipes>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        }else {
            while (cursor.moveToNext()){
                String id=cursor.getString(0);
                String name=cursor.getString(1);
                String image=cursor.getString(2);
                String category=cursor.getString(3);
                String label=cursor.getString(4);
                String price=cursor.getString(5);
                String description=cursor.getString(6);
                recipesList.add(new Recipes(id,name,image,category,label,price,description));
            }
        }
        newList.addAll(recipesList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mSearch = menu.findItem(R.id.menu_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        MenuItem cartItem=menu.findItem(R.id.shoppingCart);
        View actionView = cartItem.getActionView();
        cartView=(TextView) actionView.findViewById(R.id.cart_badge);

        updateCart();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(cartItem);
                System.out.println(cartItems+ " this is cart items");
                Intent i=new Intent(MainActivity.this,CartActivity.class);

                i.putExtra("cartItems",cartItems);
                startActivity(i);
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    private void updateCart() {
        cartSize=cartItems.size();
        if (cartView != null) {
            System.out.println(cartSize +" inside if");
            if (cartSize == 0) {
                if (cartView.getVisibility() != View.GONE) {
                    cartView.setVisibility(View.GONE);
                }
            } else {
                System.out.println(cartSize+" inside else");
                cartView.setText(String.valueOf(Math.min(cartSize, 99)));
                if (cartView.getVisibility() != View.VISIBLE) {
                    cartView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.menuSortDescending:
            Collections.sort(newList, new Comparator<Recipes>() {
                @Override
                public int compare(Recipes o1, Recipes o2) {
                    if (Double.valueOf(o1.getPrice())==Double.valueOf(o2.getPrice()))
                        return 0;
                    else if(Double.valueOf(o1.getPrice())<Double.valueOf(o2.getPrice()))
                        return 1;
                    else
                        return -1;

                }
            });

            adapter = new CustomAdapter(newList, MainActivity.this);
            recyclerView.setAdapter(adapter);

            return(true);
        case R.id.menuSortAscending:
            Collections.sort(newList, new Comparator<Recipes>() {
                @Override
                public int compare(Recipes o1, Recipes o2) {
                    if (Double.valueOf(o1.getPrice())==Double.valueOf(o2.getPrice()))
                        return 0;
                    else if(Double.valueOf(o1.getPrice())>Double.valueOf(o2.getPrice()))
                        return 1;
                    else
                        return -1;

                }
            });
            adapter = new CustomAdapter(newList, MainActivity.this);
            recyclerView.setAdapter(adapter);

            return(true);
        case R.id.shoppingCart: {
            // Do something
            return true;
        }
    }
        return(super.onOptionsItemSelected(item));
    }

}