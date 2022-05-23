package com.example.travel;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ExternalTransport_RecyclerViewAdapter extends RecyclerView.Adapter<ExternalTransport_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<ExternalTransport> externalTransports;

    public ExternalTransport_RecyclerViewAdapter(Context context, ArrayList<ExternalTransport> externalTransports) {
        this.context = context;
        this.externalTransports = externalTransports;
    }


    @NonNull
    @Override
    public ExternalTransport_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.external_recycler_view_row, parent, false);
        return new ExternalTransport_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExternalTransport_RecyclerViewAdapter.MyViewHolder holder, int position) {

        if (position == 0 || !externalTransports.get(position - 1).getAirport().equals(externalTransports.get(position).getAirport())) {
            holder.airportName.setVisibility(View.VISIBLE);
            holder.airportName.setText("From " + externalTransports.get(position).getAirport());
        } else {
            holder.airportName.setVisibility(View.GONE);
        }
        holder.transportType.setText("By " + externalTransports.get(position).getType());
        holder.description.setText(externalTransports.get(position).getDescription());
        if (externalTransports.get(position).getDepartureStation() != null && !externalTransports.get(position).getDepartureStation().isEmpty()) {
            holder.departureStation.setVisibility(View.VISIBLE);
            holder.departureStation.setText("Departure station: " + externalTransports.get(position).getDepartureStation());
        } else {
            holder.departureStation.setVisibility(View.GONE);
        }
        if (externalTransports.get(position).getArrivalStation() != null && !externalTransports.get(position).getArrivalStation().isEmpty()) {
            holder.arrivalStation.setVisibility(View.VISIBLE);
            holder.arrivalStation.setText("Arrival station: " + externalTransports.get(position).getArrivalStation());
        } else {
            holder.arrivalStation.setVisibility(View.GONE);
        }
        holder.time.setText("Trip duration: " + externalTransports.get(position).getTime());
        if (externalTransports.get(position).getFrequency() != null && !externalTransports.get(position).getFrequency().isEmpty()) {
            holder.frequency.setVisibility(View.VISIBLE);
            holder.frequency.setText("Frequency: " + externalTransports.get(position).getFrequency());
        } else {
            holder.frequency.setVisibility(View.GONE);
        }
        holder.price.setText("Price: " + externalTransports.get(position).getPrice() + " euro");

        if (externalTransports.get(position).getLink() != null && !externalTransports.get(position).getLink().isEmpty()) {
            holder.link.setVisibility(View.VISIBLE);
            holder.linkText.setVisibility(View.VISIBLE);
            StringBuilder hyperlink = new StringBuilder();
            hyperlink.append("<a href=\"").append(externalTransports.get(position).getLink()).append("\">").append(externalTransports.get(position).getLink()).append("</a>");
            holder.link.setText( Html.fromHtml(hyperlink.toString()));
            holder.link.setMovementMethod(LinkMovementMethod.getInstance());
            holder.link.setLinkTextColor(context.getResources().getColor(R.color.darkerBlue));
        } else {
            holder.linkText.setVisibility(View.GONE);
            holder.link.setVisibility(View.GONE);
        }

//        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(externalTransports.get(position)));
    }

    @Override
    public int getItemCount() {
        return externalTransports.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView airportName;
        TextView transportType;
        TextView description;
        TextView departureStation;
        TextView arrivalStation;
        TextView time;
        TextView frequency;
        TextView price;
        TextView linkText;
        TextView link;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            airportName = itemView.findViewById(R.id.airportName);
            transportType = itemView.findViewById(R.id.transportType);
            description = itemView.findViewById(R.id.description);
            departureStation = itemView.findViewById(R.id.departureStation);
            arrivalStation = itemView.findViewById(R.id.arrivalStation);
            time = itemView.findViewById(R.id.time);
            frequency = itemView.findViewById(R.id.frequency);
            price = itemView.findViewById(R.id.price);
            linkText = itemView.findViewById(R.id.linkText);
            link = itemView.findViewById(R.id.link);
        }
    }

    public interface ItemClickListener {
        void onItemClick(ExternalTransport externalTransport);
    }

}
