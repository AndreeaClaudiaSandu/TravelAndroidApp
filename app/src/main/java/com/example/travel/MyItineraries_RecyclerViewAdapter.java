package com.example.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyItineraries_RecyclerViewAdapter extends RecyclerView.Adapter<MyItineraries_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> cities;
    ArrayList<Integer> days;
    ArrayList<Integer> images;
    ArrayList<String> ids;
    private final ItemClickListener clickListener;

    public MyItineraries_RecyclerViewAdapter(Context context, ArrayList<String> cities, ArrayList<Integer> days, ArrayList<Integer> images,ArrayList<String> ids, ItemClickListener clickListener) {
        this.context = context;
        this.cities = cities;
        this.days = days;
        this.images = images;
        this.ids = ids;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyItineraries_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_itinerary_row, parent, false);
        return new MyItineraries_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItineraries_RecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.city.setText(cities.get(position).substring(0,1).toUpperCase() + cities.get(position).substring(1));
        if(days.get(position) == 1) {
            holder.days.setText(days.get(position)+" day");
        }else{
            holder.days.setText(days.get(position)+" days");
        }
        holder.layout.setBackgroundResource(images.get(position));
        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(ids.get(position), position));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView city;
        TextView days;
        ConstraintLayout layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.cityNameMyItinerary);
            days = itemView.findViewById(R.id.numberDaysMyItinerary);
            layout = itemView.findViewById(R.id.myItinerary_image);
        }
    }

    public interface ItemClickListener{
        void onItemClick(String s, int position);
    }
}
