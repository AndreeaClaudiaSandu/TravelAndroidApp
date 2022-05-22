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

public class InternalTransport_RecyclerViewAdapter extends RecyclerView.Adapter<InternalTransport_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<InternalTransport> internalTransports;

    public InternalTransport_RecyclerViewAdapter(Context context, ArrayList<InternalTransport> internalTransports) {
        this.context = context;
        this.internalTransports = internalTransports;
    }

    @NonNull
    @Override
    public InternalTransport_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.internal_recycler_view_row, parent, false);
        return new InternalTransport_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InternalTransport_RecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.ticket.setText(internalTransports.get(position).getType());
        holder.description.setText(internalTransports.get(position).getDescription());
        holder.price.setText("Price: "+ internalTransports.get(position).getPret() + " euro");

        StringBuilder hyperlink = new StringBuilder();
        hyperlink.append("<a href=\"").append(internalTransports.get(position).getLink()).append("\">").append(internalTransports.get(position).getLink()).append("</a>");
        holder.link.setText( Html.fromHtml(hyperlink.toString()));
        holder.link.setMovementMethod(LinkMovementMethod.getInstance());
        holder.link.setLinkTextColor(context.getResources().getColor(R.color.darkerBlue));

    }

    @Override
    public int getItemCount() {
        return internalTransports.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ticket;
        TextView description;
        TextView price;
        TextView link;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ticket = itemView.findViewById(R.id.typeOfTicket);
            description = itemView.findViewById(R.id.ticketDescription);
            price = itemView.findViewById(R.id.ticketPrice);
            link = itemView.findViewById(R.id.ticketLink);
        }
    }
}
