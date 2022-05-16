package com.example.travel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_city_description, container, false);
        ((TextView) root.findViewById(R.id.descriptionCity)).setText(" descriereeeeee");
        return root;
    }

    public void setCity(City city) {
        this.city = city;
    }
}