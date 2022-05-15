package com.example.travel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentCity extends Fragment {

    private City city;

    public FragmentCity() {
        // Required empty public constructor
    }

    public static FragmentCity newInstance(City city) {
        FragmentCity fragment = new FragmentCity();
        fragment.city = city;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.cityFrameLayout, FragmentCityDescription.newInstance(city));
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_city, container, false);
        ((TextView) root.findViewById(R.id.descriereButon)).setOnClickListener(v -> {
            Fragment fragment = FragmentCityDescription.newInstance(city);
            replaceFragment(fragment);
        });
        return root;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.cityFrameLayout, fragment);
        if (!fragmentManager.getFragments().isEmpty()) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

}