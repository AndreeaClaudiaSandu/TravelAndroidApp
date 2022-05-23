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

public class Attractions_RecyclerViewAdapter extends RecyclerView.Adapter<Attractions_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Attraction> attractions;
    private final ItemClickListener clickListener;

    public Attractions_RecyclerViewAdapter(Context context, ArrayList<Attraction> attractions, ItemClickListener clickListener) {
        this.context = context;
        this.attractions = attractions;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public Attractions_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.attraction_recycler_view_row, parent, false);
        return new Attractions_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Attractions_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.imageView.setImageResource(attractions.get(position).getImage());
        holder.attractionName.setText(attractions.get(position).getName());
        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(attractions.get(position)));
    }

    @Override
    public int getItemCount() {
        return attractions.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView attractionName;

        public MyViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.attractionImage);
            attractionName = itemView.findViewById(R.id.attractionName);
        }
    }

    public interface ItemClickListener{
        void  onItemClick(Attraction attraction);
    }
}
