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
    ArrayList<Integer> departureTime;
    int accommodationImage;
    boolean lunch;
    boolean lunchPos;

    public ItineraryAttraction_RecyclerViewAdapter(Context context, ArrayList<String> dayOrder, ArrayList<String> daysInfo, ArrayList<Attraction> attractions, ArrayList<Integer> departureTime, int accommodationImage, boolean lunch) {
        this.context = context;
        this.dayOrder = dayOrder;
        this.daysInfo = daysInfo;
        this.attractions = attractions;
        this.departureTime = departureTime;
        this.accommodationImage = accommodationImage;
        this.lunch= lunch;
        this.lunchPos =false;
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
            if(dayOrder.get(position).equals("lunch")){
                holder.attractionName.setText("Lunch");
                holder.visitDuration.setText( 120+  " minutes");
            }else {
                for (int i = 0; i < attractions.size(); i++) {
                    if (attractions.get(i).getName().equals(dayOrder.get(position))) {
                        holder.imageView.setImageResource(attractions.get(i).getImage());
                        holder.attractionName.setText(attractions.get(i).getName());
                        holder.visitDuration.setText(attractions.get(i).getVisitTime() + " minutes");
                        break;
                    }
                }
            }
        } else {
            holder.imageView.setImageResource(accommodationImage);
            holder.visitDurationTextView.setVisibility(View.INVISIBLE);
            holder.visitDuration.setVisibility(View.INVISIBLE);
            holder.attractionName.setText("accommodation");
        }
        if (position < dayOrder.size() - 1 && !dayOrder.get(position+1).equals("lunch")) {
            int hour = 0;
            int minutes =0;
            if(dayOrder.get(position).equals("lunch")){
                lunchPos=true;
                hour = (departureTime.get(position-1)+120) / 60;
                minutes = (departureTime.get(position-1)+120) % 60;
            }
            else if(lunchPos){
                hour = (departureTime.get(position-1)+120) / 60;
                minutes = (departureTime.get(position-1)+120) % 60;
            }
            else{
                hour = departureTime.get(position) / 60;
                minutes = departureTime.get(position) % 60;
            }

            String hourString;
            String minutesString;
            if(hour<10){
                hourString = "0" + Integer.toString(hour);
            }else{
                hourString = Integer.toString(hour);
            }
            if(minutes<10){
                minutesString = "0" + Integer.toString(minutes);
            }else{
                minutesString = Integer.toString(minutes);
            }

            holder.departureTime.setText(hourString + ":" + minutesString);
            String dayInfo;
            if(!lunchPos) {
               dayInfo  = daysInfo.get(position);
            }else{
                dayInfo = daysInfo.get(position-1);
            }
            if (dayInfo.startsWith("Total time:")) {
                String[] daySplit = dayInfo.split("\n", 2);
                holder.totalTime.setText(daySplit[0].substring(12) + " minutes");
                holder.travelInfo.setText(daySplit[1]);
            }
        } else {
            if(position == dayOrder.size() - 1) {
                holder.cardView.setVisibility(View.GONE);
            }else{
                holder.departureTextView.setVisibility(View.GONE);
                holder.totalTimeTextView.setVisibility(View.GONE);
                holder.travelDetailsTextView.setVisibility(View.GONE);
                holder.departureTime.setVisibility(View.GONE);
                holder.totalTime.setVisibility(View.GONE);
                holder.travelInfo.setText("You can have lunch in the area. If you don't want to, you can take a break, take some pictures or skip and go to the next attraction.");
            }
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
        TextView departureTextView;
        TextView totalTimeTextView;
        TextView travelDetailsTextView;

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
            departureTextView = itemView.findViewById(R.id.depratureTimeTextView);
            totalTimeTextView = itemView.findViewById(R.id.travelTimeTextView);
            travelDetailsTextView = itemView.findViewById(R.id.travelDetailsTextView);
        }
    }
}
