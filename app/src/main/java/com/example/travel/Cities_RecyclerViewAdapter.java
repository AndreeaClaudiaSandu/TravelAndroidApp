package com.example.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Cities_RecyclerViewAdapter extends RecyclerView.Adapter<Cities_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<City> cities;

    public Cities_RecyclerViewAdapter(Context context, ArrayList<City> cities){
        this.context = context;
        this.cities = cities;
    }

    @NonNull
    @Override
    public Cities_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row,parent,false);
        return new Cities_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Cities_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.cityName.setText(cities.get(position).getName());
        holder.imageView.setImageResource(cities.get(position).getImg());
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView cityName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cityImage);
            cityName = itemView.findViewById(R.id.cityName);
        }
    }
}
