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

public class Activities_RecyclerViewAdapter extends RecyclerView.Adapter<Activities_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Activity> activities;
    private Activities_RecyclerViewAdapter.ItemClickListener clickListener;

    public Activities_RecyclerViewAdapter(Context context, ArrayList<Activity> activities, Activities_RecyclerViewAdapter.ItemClickListener clickListener) {
        this.context = context;
        this.activities = activities;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public Activities_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_recycler_view_row, parent, false);
        return new Activities_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Activities_RecyclerViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.activityName.setText(activities.get(position).getDenumire());
        holder.activityLocation.setText(activities.get(position).getTara() + ", " +  activities.get(position).getOras());
        holder.imageView.setImageResource(activities.get(position).getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(activities.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public interface ItemClickListener {
        public  void onItemClick(Activity activity);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView activityName;
        TextView activityLocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.activityImage);
            activityName = itemView.findViewById(R.id.activityName);
            activityLocation = itemView.findViewById(R.id.activityLocation);
        }


    }
}
