package com.example.travel;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class FragmentMyItineraries extends Fragment {


    ArrayList<String> cities;
    ArrayList<String> idItineraries;
    ArrayList<Integer> numberOfDays;
    ArrayList<Integer> images;
    ArrayList<String> accommodations;

    public FragmentMyItineraries() {
        // Required empty public constructor
    }

    public static FragmentMyItineraries newInstance() {
        FragmentMyItineraries fragment = new FragmentMyItineraries();
        fragment.cities = new ArrayList<>();
        fragment.idItineraries = new ArrayList<>();
        fragment.numberOfDays = new ArrayList<>();
        fragment.images = new ArrayList<>();
        fragment.accommodations = new ArrayList<>();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setItineraries();
    }

    private void setItineraries() {

        class Background extends AsyncTask<String, String, String> implements MyItineraries_RecyclerViewAdapter.ItemClickListener {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("You have not saved any itinerary.") || s.equals("Connection error. Try again.")) {
                    Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
                } else {
                    RecyclerView recyclerView = getView().findViewById(R.id.myItineraries_recyclerView);
                    MyItineraries_RecyclerViewAdapter adapter = new MyItineraries_RecyclerViewAdapter(getContext(), cities, numberOfDays, images,idItineraries, this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                }

            }


            @Override
            protected String doInBackground(String... strings) {

                String server = LoginActivity.server.concat("getItineraries.php");
                StringBuilder result = new StringBuilder();
                try {
                    URL url = new URL(server);
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");
                    http.setDoInput(true);
                    http.setDoOutput(true);

                    OutputStream output = http.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                    Uri.Builder builder = new Uri.Builder().appendQueryParameter("idAccount", strings[0]);
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
                        String[] splitStrings = line.split("=,");
                        if (splitStrings.length == 4) {
                            idItineraries.add(splitStrings[0]);
                            cities.add(splitStrings[1]);
                            numberOfDays.add(Integer.valueOf(splitStrings[2]));
                            accommodations.add(splitStrings[3]);
                            images.add(getResources().getIdentifier(splitStrings[1].replace(" ", "_"), "drawable", getContext().getPackageName()));
                        }
                    }
                    reader.close();
                    input.close();
                    http.disconnect();
                    return result.toString();

                } catch (
                        IOException e) {
                    e.printStackTrace();
                    result = new StringBuilder(Objects.requireNonNull(e.getMessage()));
                }


                return result.toString();
            }

            @Override
            public void onItemClick(String s, int position) {

                MainActivity.setAccommodationAddress(accommodations.get(position));
                MainActivity.setNumberOfDays(numberOfDays.get(position));
                ArrayList<String> transport = new ArrayList<>();
                ArrayList<Boolean> check = new ArrayList<>();
                ArrayList<String> order = new ArrayList<>();
                String city = cities.get(position);
                ArrayList<Attraction> attractionsCity = new ArrayList<>();

                class Background2 extends AsyncTask<String, String, String> {

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (s.equals("No transport types found") || s.equals("Connection error. Try again.")) {
                            check.add(false);
                            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                        }
                        else{
                            check.add(true);
                            if(check.size()==3 && !check.contains(false)){
                                replaceFragment(FragmentItinerary.newInstance(order, attractionsCity, transport, "day1"));
                                setColorDays();
                            }
                        }
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        {

                            StringBuilder result = new StringBuilder();
                            String server = LoginActivity.server.concat("getTransportTypes.php");
                            try {
                                URL url = new URL(server);
                                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                                http.setRequestMethod("POST");
                                http.setDoInput(true);
                                http.setDoOutput(true);

                                OutputStream output = http.getOutputStream();
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                                Uri.Builder builder = new Uri.Builder().appendQueryParameter("idtraseu", strings[0]);

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
                                    transport.add(line);
                                }
                                reader.close();
                                input.close();
                                http.disconnect();
                                return result.toString();

                            } catch (IOException e) {
                                e.printStackTrace();
                                result = new StringBuilder(Objects.requireNonNull(e.getMessage()));
                            }


                            return result.toString();
                        }
                    }
                }
                Background2 background2 = new Background2();
                background2.execute(s);

                ArrayList<String> attractions = new ArrayList<>();
                ArrayList<String> days = new ArrayList<>();
                ArrayList<Integer> number = new ArrayList<>();

                class Background3 extends AsyncTask<String, String, String> {

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (s.equals("No attractions found") || s.equals("Connection error. Try again.")) {
                            check.add(false);
                            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                        }
                        else{

                            for(int i=0; i<numberOfDays.get(position);i++){
                                SortedMap<Integer, String> pairs = new TreeMap<>();
                                String day = "day"+(i+1);
                                order.add(day);
                                order.add("accommodation");

                                for(int j=0;j<days.size();j++){
                                    if(days.get(j).equals(day)){
                                        pairs.put(number.get(j), attractions.get(j));
                                    }
                                }
                                Set set = pairs.entrySet();
                                Iterator iterator = set.iterator();

                                while (iterator.hasNext()) {
                                    Map.Entry entry = (Map.Entry) iterator.next();
                                    order.add((String) entry.getValue());
                                }

                                order.add("accommodation");
                            }

                            check.add(true);
                            if(check.size()==3 && !check.contains(false)){
                                replaceFragment(FragmentItinerary.newInstance(order, attractionsCity, transport, "day1"));
                                setColorDays();
                            }

                        }
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        {

                            StringBuilder result = new StringBuilder();
                            String server = LoginActivity.server.concat("getAttractionsSavedItinerary.php");
                            try {
                                URL url = new URL(server);
                                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                                http.setRequestMethod("POST");
                                http.setDoInput(true);
                                http.setDoOutput(true);

                                OutputStream output = http.getOutputStream();
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                                Uri.Builder builder = new Uri.Builder().appendQueryParameter("idtraseu", strings[0]);

                                String data = builder.build().getEncodedQuery();
                                writer.write(data);
                                writer.flush();
                                writer.close();
                                output.close();

                                InputStream input = http.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.ISO_8859_1));
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    String[] splits = line.split("=,");
                                    if(splits.length==3){
                                        days.add(splits[0]);
                                        number.add(Integer.valueOf(splits[1]));
                                        attractions.add(splits[2]);
                                    }
                                    result.append(line);

                                }
                                reader.close();
                                input.close();
                                http.disconnect();
                                return result.toString();

                            } catch (IOException e) {
                                e.printStackTrace();
                                result = new StringBuilder(Objects.requireNonNull(e.getMessage()));
                            }


                            return result.toString();
                        }
                    }
                }
                Background3 background3 = new Background3();
                background3.execute(s);

                class GetAttractions extends AsyncTask<String, String, String>  {

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (s.equals("Connection error. Try again.") || s.equals("No attractions")) {
                            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                            check.add(false);
                        } else {
                          check.add(true);
                          Log.i("attractions", attractionsCity.toString());
                          if(check.size()==3 && !check.contains(false)){
                              replaceFragment(FragmentItinerary.newInstance(order, attractionsCity, transport, "day1"));
                              setColorDays();
                          }
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
                                        attractionsCity.add(new Attraction(fields[0],city, fields[1],fields[2],fields[3],fields[4],fields[5],fields[6], fields[7], getResources().getIdentifier(fields[0].replace(" ", "_"), "drawable", getContext().getPackageName())));

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

                }
                GetAttractions getAttractions = new GetAttractions();
                getAttractions.execute(city);

            }
        }

        Background background = new Background();
        background.execute(Integer.toString(LoginActivity.idAccount));


    }

    private void replaceFragment(FragmentItinerary fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        if (!fragmentManager.getFragments().isEmpty()) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_itineraries, container, false);
    }

    public void setColorDays(){
        ((TextView) getActivity().findViewById(R.id.day1)).setBackground(getResources().getDrawable(R.drawable.day_shape));
        ((TextView) getActivity().findViewById(R.id.day1)).setTextColor(getResources().getColor(R.color.loginDarkBlue));
        ((TextView) getActivity().findViewById(R.id.day2)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
        ((TextView) getActivity().findViewById(R.id.day2)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) getActivity().findViewById(R.id.day3)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
        ((TextView) getActivity().findViewById(R.id.day3)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) getActivity().findViewById(R.id.day4)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
        ((TextView) getActivity().findViewById(R.id.day4)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) getActivity().findViewById(R.id.day5)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
        ((TextView) getActivity().findViewById(R.id.day5)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) getActivity().findViewById(R.id.day6)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
        ((TextView) getActivity().findViewById(R.id.day6)).setTextColor(getResources().getColor(R.color.white));
        ((TextView) getActivity().findViewById(R.id.day7)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
        ((TextView) getActivity().findViewById(R.id.day7)).setTextColor(getResources().getColor(R.color.white));
    }
}