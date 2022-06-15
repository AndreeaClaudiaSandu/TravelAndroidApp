package com.example.travel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentCityAttraction extends Fragment {

    private Attraction attraction;

    public FragmentCityAttraction() {
        // Required empty public constructor
    }

    public static FragmentCityAttraction newInstance(Attraction attraction) {
        FragmentCityAttraction fragment = new FragmentCityAttraction();
        fragment.setAttraction(attraction);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_city_attraction, container, false);
        ((TextView) root.findViewById(R.id.specificAttractionName)).setText(attraction.getName());
        ((ImageView) root.findViewById(R.id.specificAttractionImage)).setImageResource(attraction.getImage());
        ((TextView) root.findViewById(R.id.specificAttarctionDescription)).setText(Html.fromHtml( attraction.getDescription()));
        ((TextView) root.findViewById(R.id.specificAttractionTimetable)).setText(Html.fromHtml( attraction.getTimetable()));
        ((TextView) root.findViewById(R.id.specificAttractionPrices)).setText(Html.fromHtml( attraction.getPrice()));
        ((TextView) root.findViewById(R.id.specificAttractionVisitDuration)).setText(Integer.toString(attraction.getVisitTime()));
        ((TextView) root.findViewById(R.id.specificAttractionLocation)).setText(attraction.getLocation());

        StringBuilder hyperlink = new StringBuilder();
        hyperlink.append("<a href=\"").append(attraction.getLink()).append("\">").append(attraction.getLink()).append("</a>");
        ((TextView) root.findViewById(R.id.specificAttractionLink)).setText(Html.fromHtml(hyperlink.toString()));
        ((TextView) root.findViewById(R.id.specificAttractionLink)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) root.findViewById(R.id.specificAttractionLink)).setLinkTextColor(getResources().getColor(R.color.darkerBlue));

        return root;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }
}