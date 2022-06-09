package com.example.travel;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItineraryAttraction_RecyclerViewAdapter extends RecyclerView.Adapter<ItineraryAttraction_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> daysInfo;
    ArrayList<Attraction> attractions;
    ArrayList<String> dayOrder;
    int accommodationImage;

    public ItineraryAttraction_RecyclerViewAdapter(Context context, ArrayList<String> dayOrder, ArrayList<String> daysInfo, ArrayList<Attraction> attractions, int accommodationImage) {
        this.context = context;
        this.dayOrder = dayOrder;
        this.daysInfo = daysInfo;
        this.attractions = attractions;
        this.accommodationImage = accommodationImage;
    }

    @NonNull
    @Override
    public ItineraryAttraction_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.intinerary_row, parent, false);
        return new ItineraryAttraction_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryAttraction_RecyclerViewAdapter.MyViewHolder holder, int position) {
        if (position != 0 && position != dayOrder.size()-1) {
            for (int i = 0; i < attractions.size(); i++) {
                if (attractions.get(i).getName().equals(dayOrder.get(position))) {
                    holder.imageView.setImageResource(attractions.get(i).getImage());
                    holder.attractionName.setText(attractions.get(i).getName());
                    holder.visitDuration.setText(attractions.get(i).getVisitTime() + "minutes");
                    break;
                }
            }
        } else {
            holder.imageView.setImageResource(accommodationImage);
            holder.visitDurationTextView.setVisibility(View.INVISIBLE);
            holder.visitDuration.setVisibility(View.INVISIBLE);
            holder.attractionName.setText("accommodation");
        }
        if (position < dayOrder.size() - 1) {
            if (position == 0) {
                holder.departureTime.setText("9am");
            } else {
                holder.departureTime.setText("nush");
            }
            String dayInfo = daysInfo.get(position);
            if (dayInfo.startsWith("Total time:")) {
                String[] daySplit = dayInfo.split("\n", 2);
                holder.totalTime.setText(daySplit[0].substring(12));
                holder.travelInfo.setText(daySplit[1]);
            }
        } else {
            holder.cardView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dayOrder.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView attractionName;
        TextView visitDuration;
        TextView visitDurationTextView;
        TextView departureTime;
        TextView totalTime;
        TextView travelInfo;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.attractionItineraryImage);
            attractionName = itemView.findViewById(R.id.attractionItineraryName);
            visitDuration = itemView.findViewById(R.id.visitDuration);
            visitDurationTextView = itemView.findViewById(R.id.visitDurationTextView);
            departureTime = itemView.findViewById(R.id.departureTime);
            totalTime = itemView.findViewById(R.id.totalTravelTime);
            travelInfo = itemView.findViewById(R.id.travelDetails);
            cardView = itemView.findViewById(R.id.travelDetailsCardView);
        }
    }
}
