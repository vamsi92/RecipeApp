package com.strikerapps.recipeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class CustomMapAdapter extends BaseAdapter {
    private final ArrayList mData;
    private ArrayList<Recipes> shoppingCartList;
    private Context context;

    public CustomMapAdapter(Map<String,Integer> map, ArrayList<Recipes> shoppingCartList,Context context) {
        this.shoppingCartList=shoppingCartList;
        mData=new ArrayList();
        mData.addAll(map.entrySet());
        this.context=context;
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String,Integer> getItem(int position) {
        return (Map.Entry)mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listview,null,true);



        ImageView imageView=(ImageView)itemView.findViewById(R.id.imageCart);
        Map.Entry<String,Integer> item=getItem(position);
        int listPos=getRecipePosition(shoppingCartList,item.getKey());
        Picasso.with(context).load(shoppingCartList.get(listPos).getImage()).into(imageView);

        TextView recipeName=(TextView)itemView.findViewById(R.id.textViewNameCart);
        recipeName.setText(item.getKey());

        TextView recipePrice=(TextView)itemView.findViewById(R.id.textViewPriceCart);
        recipePrice.setText(shoppingCartList.get(listPos).getPrice());

        TextView quantity=(TextView)itemView.findViewById(R.id.textViewQuantity);
        quantity.append(String.valueOf(item.getValue()));



        return itemView;//super.getView(position, convertView, parent);

    }

    int getRecipePosition(ArrayList<Recipes> rpList, String recipeName){
        int position=0;
        for(int i=0;i<rpList.size();i++){
            if(rpList.get(i).getName()==recipeName){
                position=i;
                break;
            }
        }
        return position;
    }
}
