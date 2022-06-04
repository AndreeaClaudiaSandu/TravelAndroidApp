package com.example.travel;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FragmentCityDescription extends Fragment {

    private City city;

    public FragmentCityDescription() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentCityDescription newInstance(City city) {
        FragmentCityDescription fragment = new FragmentCityDescription();
        fragment.setCity(city);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TextView) getActivity().findViewById(R.id.descriptionButton)).setOnClickListener(v -> {
            Fragment fragment = FragmentCityDescription.newInstance(city);
            ((MainActivity) getActivity()).replaceFragment(fragment);
           });

        ((TextView) getActivity().findViewById(R.id.transportButton)).setOnClickListener(v -> {
            Fragment fragment = FragmentCityTransport.newInstance(city);
            ((MainActivity) getActivity()).replaceFragment(fragment);
           });

        ((TextView) getActivity().findViewById(R.id.attractionButton)).setOnClickListener(v -> {
            Fragment fragment = FragmentCityAttractions.newInstance(city);
            ((MainActivity) getActivity()).replaceFragment(fragment);
           });

        ((TextView) getActivity().findViewById(R.id.configureItinerary)).setOnClickListener(v -> {
            Fragment fragment = FragmentCityConfigureItinerary.newInstance(city);
            ((MainActivity) getActivity()).replaceFragment(fragment);
            });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_city_description, container, false);
        ((TextView) root.findViewById(R.id.descriptionCity)).setText(Html.fromHtml( city.getDescription()));
        return root;
    }

    public void setCity(City city) {
        this.city = city;
    }
}