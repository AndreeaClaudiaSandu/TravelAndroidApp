package com.example.travel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PickAttractions_RecyclerViewAdapter extends RecyclerView.Adapter<PickAttractions_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Attraction> attractions;
    private final ItemClickListener clickListener;

    public PickAttractions_RecyclerViewAdapter(Context context, ArrayList<Attraction> attractions, PickAttractions_RecyclerViewAdapter.ItemClickListener clickListener){
        this.context = context;
        this.attractions = attractions;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PickAttractions_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pick_attraction_recycler_view_row, parent, false);
        return new PickAttractions_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PickAttractions_RecyclerViewAdapter.MyViewHolder holder, int position) {
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
        CheckBox checkBox;

        public MyViewHolder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.attractionImage);
            attractionName = itemView.findViewById(R.id.attractionName);
            checkBox = itemView.findViewById(R.id.pickAttractionCheckBox);
        }
    }

    public interface ItemClickListener{
        void  onItemClick(Attraction attraction);
    }
}
