package com.example.travel;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class FragmentCityConfigureItinerary extends Fragment {

    public static ArrayList<Attraction> attractions = new ArrayList<>();
    City city;

    public FragmentCityConfigureItinerary() {
        // Required empty public constructor
    }

    public static FragmentCityConfigureItinerary newInstance(City city) {
        FragmentCityConfigureItinerary fragment = new FragmentCityConfigureItinerary();
        fragment.attractions = new ArrayList<>();
        fragment.city = city;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAttractions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_city_configure_itinerary, container, false);
        root.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        return root;
    }

    private void setAttractions(){

        class GetAttractions extends AsyncTask<String, String, String> implements PickAttractions_RecyclerViewAdapter.ItemClickListener  {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("Connection error. Try again.") || s.equals("No attractions")) {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                } else {
                    RecyclerView recyclerView = getView().findViewById(R.id.chooseAttractionsRecyclerView);
                    PickAttractions_RecyclerViewAdapter adapter = new PickAttractions_RecyclerViewAdapter(getContext(), attractions, this );
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                }
            }

            @Override
            protected String doInBackground(String... strings) {
                {

                    String server = LoginActivity.server.concat("getAttractions.php");
                    StringBuilder result = new StringBuilder();
                    try {
                        URL url = new URL(server);
                        HttpURLConnection http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("POST");
                        http.setDoInput(true);
                        http.setDoOutput(true);

                        OutputStream output = http.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                        Uri.Builder builder = new Uri.Builder().appendQueryParameter("city", strings[0]);
                        String data = builder.build().getEncodedQuery();
                        writer.write(data);
                        writer.flush();
                        writer.close();
                        output.close();

                        InputStream input = http.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.ISO_8859_1));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                            String[] fields = line.split("=,");
                            if (fields.length > 1) {
                                attractions.add(new Attraction(fields[0],city.getName(), fields[1],fields[2],fields[3],fields[4],fields[5],fields[6], fields[7], getResources().getIdentifier(fields[0].replace(" ", "_"), "drawable", getContext().getPackageName())));

                            }
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
            }


            @Override
            public void onItemClick(Attraction attraction) {
                Fragment fragment = FragmentCityAttraction.newInstance(attraction);
                replaceFragment(fragment);
            }
        }
        GetAttractions attractions = new GetAttractions();
        attractions.execute(city.getName());
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        if (!fragmentManager.getFragments().isEmpty()) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }



}