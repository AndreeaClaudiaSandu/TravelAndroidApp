package com.example.travel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCity extends Fragment {

    private static final String ARG_PARAM = "param";
    private static ArrayList<City> cities = new ArrayList<>();

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    private String cityName;

    public FragmentCity() {
        // Required empty public constructor
    }

    public static FragmentCity newInstance(ArrayList cities2, String cityName) {
        Log.d("instance",Integer.toString( cities.size()));
        Log.d("name", cityName);
        FragmentCity fragment = new FragmentCity();
        Bundle args = new Bundle();
//        fragment.setCities(cities);
//        args.putStringArrayList(ARG_PARAM, cities);
        cities = cities2;

        args.putString("cityName", cityName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.i("cityName", getArguments().getString("cityName"));
            setCityName(getArguments().getString("cityName"));

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_city, container, false);
        ((TextView) root.findViewById(R.id.textView5)).setText(cityName);
//        ((TextView) root.findViewById(R.id.textView2)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = FragmentPickCity.newInstance(cities);
//                ((MainActivity) getActivity()).replaceFragment(fragment);
//            }
//        });
        return root;
    }

    public void nextFragment(View view){

    }
}