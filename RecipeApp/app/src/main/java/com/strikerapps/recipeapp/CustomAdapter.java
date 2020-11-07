package com.strikerapps.recipeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Recipes> dataSet;
    private List<Recipes> exampleListFull;
    private Context context;

    @Override
    public Filter getFilter() {
        return recipeFilter;
    }

    private Filter recipeFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Recipes> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Recipes item : exampleListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataSet.clear();
            dataSet.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public CustomAdapter(ArrayList<Recipes> data, Context context) {
        this.dataSet = data;
        this.context=context;
        exampleListFull = new ArrayList<>(dataSet);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewPrice = holder.textViewPrice;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewPrice.setText(dataSet.get(listPosition).getPrice());
        Picasso.with(context).load(dataSet.get(listPosition).getImage()).into(imageView);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public ArrayList<Recipes> getData(){

        return dataSet;
    }
}