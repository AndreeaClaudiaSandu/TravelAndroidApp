package com.example.travel;

import android.annotation.SuppressLint;
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
    private final ItemClickListener clickListener;

    public Cities_RecyclerViewAdapter(Context context, ArrayList<City> cities, ItemClickListener clickListener) {
        this.context = context;
        this.cities = cities;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public Cities_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.city_recycler_view_row, parent, false);
        return new Cities_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Cities_RecyclerViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.cityName.setText(cities.get(position).getName());
        holder.imageView.setImageResource(cities.get(position).getImg());

        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(cities.get(position)));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView cityName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cityImage);
            cityName = itemView.findViewById(R.id.cityName);
        }
    }

    public interface ItemClickListener{
        void onItemClick(City city);
    }
}
