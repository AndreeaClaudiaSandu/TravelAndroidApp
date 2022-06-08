package com.example.travel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentItinerary extends Fragment {

    ArrayList<String> attractionsOrder;
    ArrayList<Attraction> attractions;
    ArrayList<String> transport;
    TextView orderTextView;
    TextView routeTextView;

    public FragmentItinerary() {
        // Required empty public constructor
    }


    public static FragmentItinerary newInstance(ArrayList<String> attractionsOrder, ArrayList<Attraction> attractions, ArrayList<String> transport) {
        FragmentItinerary fragment = new FragmentItinerary();
        fragment.attractionsOrder = attractionsOrder;
        fragment.attractions = attractions;
        fragment.transport = transport;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private String getTransportInfo() {

        StringBuilder info = new StringBuilder();
        String accommodationAddress = MainActivity.accommodationAddress;
        accommodationAddress = accommodationAddress.replace(" ", "%20").replace(",", "%2C").replace("à", "%C3%A0");
        String location;

        for (int i = 0; i < attractionsOrder.size() - 1; i++) {
            String location1 = attractionsOrder.get(i);
            String location1Adress = "";
            if (location1.equals("accommodation")) {
                location1Adress = MainActivity.accommodationAddress;
            } else {
                for (int j = 0; j < attractions.size(); j++) {
                    if (attractions.get(j).getName().equals(location1)) {
                        location1Adress = attractions.get(j).getLocation();
                    }
                }
            }
            String location2 = attractionsOrder.get(i + 1);
            String location2Adress = "";
            if (location2.equals("accommodation")) {
                location2Adress = MainActivity.accommodationAddress;
            } else {
                for (int j = 0; j < attractions.size(); j++) {
                    if (attractions.get(j).getName().equals(location2)) {
                        location2Adress = attractions.get(j).getLocation();
                    }
                }
            }
            info.append("From ").append(location1).append(" to ").append(location2).append("\n");

            location1Adress = location1Adress.replace(" ", "%20").replace(",", "%2C").replace("à", "%C3%A0");
            location2Adress = location2Adress.replace(" ", "%20").replace(",", "%2C").replace("à", "%C3%A0");

            String url = generateURL(location1Adress, location2Adress);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                JSONObject directionObject = new JSONObject(response.body().string());
                String route = parseObject(directionObject);
                Log.i("route", directionObject.toString());
                info.append("\n" + route);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        return info.toString();
    }

    private String parseObject(JSONObject directionObject) {
        StringBuilder route = new StringBuilder();
        JSONArray routes = directionObject.optJSONArray("routes");
        if (routes != null && routes.optJSONObject(0) != null) {
            JSONArray legs = routes.optJSONObject(0).optJSONArray("legs");
            if (legs != null && legs.optJSONObject(0) != null) {
                JSONObject duration = legs.optJSONObject(0).optJSONObject("duration");
                if (duration != null) {
                    int seconds = duration.optInt("value");
                    int minutes = seconds / 60;
                    route.append("Total time: ").append(minutes).append("\n");
                }
                JSONArray steps = legs.optJSONObject(0).optJSONArray("steps");
                if (steps != null) {
                    for (int i = 0; i < steps.length(); i++) {
                        if (steps.optJSONObject(i) != null) {
                            route.append(i).append(":\n");
                            JSONObject durationStep = steps.optJSONObject(i).optJSONObject("duration");
                            int seconds = durationStep.optInt("value");
                            int minutesStep = seconds / 60;
                            route.append(minutesStep).append(" minutes\n");
                            String travelMode = steps.optJSONObject(i).optString("travel_mode");
                            route.append("travel mode: ").append(travelMode).append("\n");
                            if (travelMode.equals("TRANSIT")) {
                                JSONObject transitDetails = steps.optJSONObject(i).optJSONObject("transit_details");
                                if (transitDetails != null) {
                                    JSONObject departureStop = transitDetails.optJSONObject("departure_stop");
                                    if (departureStop != null) {
                                        String departureName = departureStop.optString("name");
                                        route.append("departure stop: ").append(departureName).append("\n");
                                    }
                                    JSONObject arrivalStop = transitDetails.optJSONObject("arrival_stop");
                                    if (arrivalStop != null) {
                                        String arrivalName = arrivalStop.optString("name");
                                        route.append("arrival stop: ").append(arrivalName).append("\n");
                                    }
                                    JSONObject line = transitDetails.optJSONObject("line");
                                    if (line != null) {
                                        JSONObject vehicle = line.optJSONObject("vehicle");
                                        if (vehicle != null) {
                                            String vehicleType = vehicle.optString("type");
                                            route.append("vehicle type: ").append(vehicleType).append("\n");
                                        }
                                        String shortName = line.optString("short_name");
                                        route.append("vehicle name: ").append(shortName).append("\n");
                                        int stops = line.optInt("num_stops");
                                        route.append("number of stops: ").append(stops).append("\n");
                                    }
                                }
                            }
                        }
                        route.append("\n");
                    }
                }
            }
        }
        return route.toString();
    }

    private String generateURL(String location1, String location2) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?origin=");
        url.append(location1).append("&destination=").append(location2);
        if (transport.contains("car")) {
            url.append("&mode=driving");
        } else {
            url.append("&mode=transit&transit_mode=");
            if (transport.contains("bus")) {
                url.append("bus|");
            }
            if (transport.contains("subway")) {
                url.append("subway|");
            }
            if (transport.contains("tram")) {
                url.append("tram|");
            }
            if (transport.contains("train")) {
                url.append("train|");
            }
            url.deleteCharAt(url.length() - 1);
        }
        url.append("&key=").append(ConfigureItinerary.API_KEY);

        return url.toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_itinerary, container, false);
        orderTextView = root.findViewById(R.id.attractionsOrder);
        orderTextView.setText(attractionsOrder.toString());

        routeTextView = root.findViewById(R.id.routeTextView);
        routeTextView.setText(getTransportInfo());
        setTheDaysVisibility();

        return root;
    }

    private void setTheDaysVisibility() {
        getActivity().findViewById(R.id.linearLayoutDays).setVisibility(View.VISIBLE);
        if (MainActivity.numberOfDays == 1) {
            getActivity().findViewById(R.id.day1).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day2).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day3).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day4).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day5).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day6).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day7).setVisibility(View.INVISIBLE);
        }
        if (MainActivity.numberOfDays == 2) {
            getActivity().findViewById(R.id.day1).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day2).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day3).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day4).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day5).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day6).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day7).setVisibility(View.INVISIBLE);
        }
        if (MainActivity.numberOfDays == 3) {
            getActivity().findViewById(R.id.day1).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day2).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day3).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day4).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day5).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day6).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day7).setVisibility(View.INVISIBLE);
        }
        if (MainActivity.numberOfDays >= 5) {
            getActivity().findViewById(R.id.day1).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day2).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day3).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day4).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day5).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day6).setVisibility(View.INVISIBLE);
            getActivity().findViewById(R.id.day7).setVisibility(View.INVISIBLE);
        }
        if (MainActivity.numberOfDays == 7) {
            getActivity().findViewById(R.id.day1).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day2).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day3).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day4).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day5).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day6).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.day7).setVisibility(View.VISIBLE);
        }
    }
}