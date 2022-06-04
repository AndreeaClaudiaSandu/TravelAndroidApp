package com.example.travel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class FragmentPickCity extends Fragment {

    ArrayList<City> cities = new ArrayList<>();

    public FragmentPickCity() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCities();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pick_city, container, false);
    }

    private void setCities() {

        class GetCities extends AsyncTask<String, String, String> implements Cities_RecyclerViewAdapter.ItemClickListener{

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("Connection error. Try again.")) {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                } else {
                    RecyclerView recyclerView = getView().findViewById(R.id.citiesRecyclerView);
                    Cities_RecyclerViewAdapter adapter = new Cities_RecyclerViewAdapter(getContext(), cities, this );
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                }
            }

            @Override
            protected String doInBackground(String... strings) {

                String server = LoginActivity.server.concat("getCities.php");
                StringBuilder result = new StringBuilder();
                try {
                    URL url = new URL(server);
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");
                    http.setDoInput(true);

                    InputStream input = http.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.ISO_8859_1));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                        String[] fields = line.split("=,");

                        cities.add(new City(fields[0], fields[1], fields[2], fields[3] == "null" ? null: fields[3], fields[4] == "null" ? null: fields[4], getResources().getIdentifier(fields[0], "drawable", getContext().getPackageName())));
                    }
                    reader.close();
                    input.close();
                    http.disconnect();
                    return result.toString();

                } catch (
                        IOException e) {
                    e.printStackTrace();
                    return (new StringBuilder(Objects.requireNonNull(e.getMessage()))).toString();
                }

            }

            @Override
            public void onItemClick(City city) {
                Toast.makeText(getContext(), city.getName(), Toast.LENGTH_SHORT).show();
                Fragment fragment = FragmentCity.newInstance(city);
                ((MainActivity) getActivity()).replaceFragment(fragment);

            }
        }

        GetCities cities = new GetCities();
        cities.execute();
    }

}