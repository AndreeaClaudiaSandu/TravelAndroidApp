package com.example.travel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentItinerary extends Fragment {

    static ArrayList<String> attractionsOrder;
    ArrayList<Attraction> attractions;
    static ArrayList<String>  transport;
    String dayNumber;
    ArrayList<String> dayOrder;
    ArrayList<String> daysInfo;
    long seconds;
    ArrayList<Integer> departureTime;
    MapView mapView;
    GoogleMap googleMap;
    ArrayList<String> locationsOrderAddress;
    ArrayList<LatLng> latLngs;
    static String city;
//    boolean lunch;
//    boolean extra;


    public FragmentItinerary() {
        // Required empty public constructor
    }

    public static FragmentItinerary newInstance(ArrayList<String> attractionsOrder, ArrayList<Attraction> attractions, ArrayList<String> transport, String dayNumber) {
        FragmentItinerary fragment = new FragmentItinerary();
        fragment.attractionsOrder = attractionsOrder;
        fragment.attractions = attractions;
        fragment.transport = transport;
        fragment.dayNumber = dayNumber;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDayOrder();
        addDay1Listener();
        addDay2Listener();
        addDay3Listener();
        addDay4Listener();
        addDay5Listener();
        addDay6Listener();
        addDay7Listener();
    }

    private void addDay7Listener() {
        getActivity().findViewById(R.id.day7).setOnClickListener(v -> {
            Fragment fragment = FragmentItinerary.newInstance(attractionsOrder, attractions, transport, "day7");
            ((MainActivity) getActivity()).replaceFragment(fragment);
            ((TextView) getActivity().findViewById(R.id.day7)).setBackground(getResources().getDrawable(R.drawable.day_shape));
            ((TextView) getActivity().findViewById(R.id.day7)).setTextColor(getResources().getColor(R.color.loginDarkBlue));
            ((TextView) getActivity().findViewById(R.id.day1)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day1)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day3)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day3)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day4)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day4)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day5)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day5)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day6)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day6)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day2)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day2)).setTextColor(getResources().getColor(R.color.white));
        });
    }

    private void addDay6Listener() {
        getActivity().findViewById(R.id.day6).setOnClickListener(v -> {
            Fragment fragment = FragmentItinerary.newInstance(attractionsOrder, attractions, transport, "day6");
            ((MainActivity) getActivity()).replaceFragment(fragment);
            ((TextView) getActivity().findViewById(R.id.day6)).setBackground(getResources().getDrawable(R.drawable.day_shape));
            ((TextView) getActivity().findViewById(R.id.day6)).setTextColor(getResources().getColor(R.color.loginDarkBlue));
            ((TextView) getActivity().findViewById(R.id.day1)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day1)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day3)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day3)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day4)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day4)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day5)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day5)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day2)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day2)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day7)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day7)).setTextColor(getResources().getColor(R.color.white));
        });
    }

    private void addDay5Listener() {
        getActivity().findViewById(R.id.day5).setOnClickListener(v -> {
            Fragment fragment = FragmentItinerary.newInstance(attractionsOrder, attractions, transport, "day5");
            ((MainActivity) getActivity()).replaceFragment(fragment);
            ((TextView) getActivity().findViewById(R.id.day5)).setBackground(getResources().getDrawable(R.drawable.day_shape));
            ((TextView) getActivity().findViewById(R.id.day5)).setTextColor(getResources().getColor(R.color.loginDarkBlue));
            ((TextView) getActivity().findViewById(R.id.day1)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day1)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day3)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day3)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day4)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day4)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day2)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day2)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day6)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day6)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day7)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day7)).setTextColor(getResources().getColor(R.color.white));
        });
    }

    private void addDay4Listener() {
        getActivity().findViewById(R.id.day4).setOnClickListener(v -> {
            Fragment fragment = FragmentItinerary.newInstance(attractionsOrder, attractions, transport, "day4");
            ((MainActivity) getActivity()).replaceFragment(fragment);
            ((TextView) getActivity().findViewById(R.id.day4)).setBackground(getResources().getDrawable(R.drawable.day_shape));
            ((TextView) getActivity().findViewById(R.id.day4)).setTextColor(getResources().getColor(R.color.loginDarkBlue));
            ((TextView) getActivity().findViewById(R.id.day1)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day1)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day3)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day3)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day2)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day2)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day5)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day5)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day6)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day6)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day7)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day7)).setTextColor(getResources().getColor(R.color.white));
        });
    }

    private void addDay3Listener() {
        getActivity().findViewById(R.id.day3).setOnClickListener(v -> {
            Fragment fragment = FragmentItinerary.newInstance(attractionsOrder, attractions, transport, "day3");
            ((MainActivity) getActivity()).replaceFragment(fragment);
            ((TextView) getActivity().findViewById(R.id.day3)).setBackground(getResources().getDrawable(R.drawable.day_shape));
            ((TextView) getActivity().findViewById(R.id.day3)).setTextColor(getResources().getColor(R.color.loginDarkBlue));
            ((TextView) getActivity().findViewById(R.id.day1)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day1)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day2)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day2)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day4)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day4)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day5)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day5)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day6)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day6)).setTextColor(getResources().getColor(R.color.white));
            ((TextView) getActivity().findViewById(R.id.day7)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day7)).setTextColor(getResources().getColor(R.color.white));
        });
    }

    private void addDay2Listener() {
        getActivity().findViewById(R.id.day2).setOnClickListener(v -> {
            Fragment fragment = FragmentItinerary.newInstance(attractionsOrder, attractions, transport, "day2");
            ((MainActivity) getActivity()).replaceFragment(fragment);
            ((TextView) getActivity().findViewById(R.id.day2)).setBackground(getResources().getDrawable(R.drawable.day_shape));
            ((TextView) getActivity().findViewById(R.id.day2)).setTextColor(getResources().getColor(R.color.loginDarkBlue));
            ((TextView) getActivity().findViewById(R.id.day1)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
            ((TextView) getActivity().findViewById(R.id.day1)).setTextColor(getResources().getColor(R.color.white));
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
        });
    }

    private void addDay1Listener() {
        getActivity().findViewById(R.id.day1).setOnClickListener(v -> {
            Fragment fragment = FragmentItinerary.newInstance(attractionsOrder, attractions, transport, "day1");
            ((MainActivity) getActivity()).replaceFragment(fragment);
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

        });
    }

    private void setDayOrder() {
        dayOrder = new ArrayList<>();
        int index = attractionsOrder.indexOf(dayNumber);
        index++;
        while (index < attractionsOrder.size() && !attractionsOrder.get(index).equals("day1") && !attractionsOrder.get(index).equals("day2") && !attractionsOrder.get(index).equals("day3") && !attractionsOrder.get(index).equals("day4") && !attractionsOrder.get(index).equals("day5") && !attractionsOrder.get(index).equals("day6") && !attractionsOrder.get(index).equals("day7")) {
            dayOrder.add(attractionsOrder.get(index));
            index++;
        }
    }

    private void getTransportInfo() {
        locationsOrderAddress = new ArrayList<>();
        String accommodationAddress = MainActivity.accommodationAddress;
        accommodationAddress = accommodationAddress.replace(" ", "%20").replace(",", "%2C").replace("à", "%C3%A0");

        daysInfo = new ArrayList<>();
        departureTime = new ArrayList<>();
        String route = null;
        int minutesVisit = 0;
        for (int i = 0; i < dayOrder.size() - 1; i++) {
            String location1 = dayOrder.get(i);
            String location1Address = "";
            if (location1.equals("accommodation")) {
                location1Address = MainActivity.accommodationAddress;
            } else {
                for (int j = 0; j < attractions.size(); j++) {
                    if(city==null || city.isEmpty()){
                        city = attractions.get(i).getCity();
                    }
                    if (attractions.get(j).getName().equals(location1)) {
                        location1Address = attractions.get(j).getLocation();
                        minutesVisit += attractions.get(j).getVisitTime();
                    }
                }
            }
            String location2 = dayOrder.get(i + 1);
            String location2Address = "";
            if (location2.equals("accommodation")) {
                location2Address = MainActivity.accommodationAddress;
            } else {
                for (int j = 0; j < attractions.size(); j++) {
                    if (attractions.get(j).getName().equals(location2)) {
                        location2Address = attractions.get(j).getLocation();
                    }
                }
            }
            location1Address = location1Address.replace(" ", "%20").replace(",", "%2C").replace("à", "%C3%A0");
            location2Address = location2Address.replace(" ", "%20").replace(",", "%2C").replace("à", "%C3%A0");

            locationsOrderAddress.add(location1Address);

            String url = generateURL(location1Address, location2Address, i, route);

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                JSONObject directionObject = new JSONObject(response.body().string());
                Log.i("direction object", directionObject.toString());
                route = parseObject(directionObject);
                daysInfo.add(route);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
//        Log.i("minutesVisittt", Integer.toString(minutesVisit));
//        minutesVisit+=(dayOrder.size()-2)*30;
//        Log.i("minutesVisitttAfter", Integer.toString(minutesVisit));
//        if(minutesVisit<=300){
//            extra = true;
//        }else if(minutesVisit<=480){
//            lunch = true;
//        }
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
                if (transport.contains("car")) {
                    route.append("Travel mode: driving");
                } else {
                    JSONArray steps = legs.optJSONObject(0).optJSONArray("steps");
                    if (steps != null) {
                        for (int i = 0; i < steps.length(); i++) {
                            if (steps.optJSONObject(i) != null) {
                                if(i!=0){
                                    route.append("\n");
                                }
                                route.append("Step ").append(i + 1).append(":\n");
                                JSONObject durationStep = steps.optJSONObject(i).optJSONObject("duration");
                                int seconds = durationStep.optInt("value");
                                int minutesStep = seconds / 60;
                                route.append("Duration: ").append(minutesStep).append(" minutes\n");
                                String travelMode = steps.optJSONObject(i).optString("travel_mode");
                                route.append("Travel mode: ").append(travelMode.toLowerCase()).append("\n");

                                if (travelMode.equals("TRANSIT")) {
                                    JSONObject transitDetails = steps.optJSONObject(i).optJSONObject("transit_details");
                                    if (transitDetails != null) {
                                        JSONObject departureStop = transitDetails.optJSONObject("departure_stop");
                                        if (departureStop != null) {
                                            String departureName = departureStop.optString("name");
                                            route.append("Departure stop: ").append(departureName).append("\n");
                                        }
                                        JSONObject arrivalStop = transitDetails.optJSONObject("arrival_stop");
                                        if (arrivalStop != null) {
                                            String arrivalName = arrivalStop.optString("name");
                                            route.append("Arrival stop: ").append(arrivalName).append("\n");
                                        }
                                        JSONObject line = transitDetails.optJSONObject("line");
                                        if (line != null) {
                                            JSONObject vehicle = line.optJSONObject("vehicle");
                                            if (vehicle != null) {
                                                String vehicleType = vehicle.optString("type");
                                                route.append("Vehicle type: ").append(vehicleType).append("\n");
                                            }
                                            String shortName = line.optString("short_name");
                                            route.append("Vehicle name: ").append(shortName).append("\n");
                                        }
                                        int stops = transitDetails.optInt("num_stops");
                                        route.append("Number of stops: ").append(stops).append("\n");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return route.toString();
    }

    private String generateURL(String location1, String location2, int position, String route) {
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
        if (position == 0) {
            setSecondsOffset(url);
            departureTime.add(540);
        } else {
            String[] daySplit = route.split("\n", 2);
            String minutesForTraveling = daySplit[0].substring(12);
            Log.i("minutesForTraveling", minutesForTraveling);
            int minutesTravel = Integer.valueOf(minutesForTraveling);
            int secondsForTraveling = minutesTravel * 60;
            int minutesVisitTime = 0;

            Log.i("minutesAttraction", dayOrder.get(position));
            for (int i = 0; i < attractions.size(); i++) {
                if (attractions.get(i).getName().equals(dayOrder.get(position))) {
                    minutesVisitTime = attractions.get(i).getVisitTime();
                    break;
                }
            }

            int secondsVisitTime = minutesVisitTime * 60;

            Log.i("minutesVisit", Integer.toString(minutesVisitTime));
            Log.i("minutesPrevious", Integer.toString(departureTime.get(position - 1)));
            departureTime.add(departureTime.get(position - 1) + minutesTravel + minutesVisitTime);
            Log.i("minutesDeparture", Integer.toString(departureTime.get(position)));
            seconds += secondsForTraveling;
            seconds += secondsVisitTime;

        }
        url.append("&departure_time=").append(seconds);
        url.append("&key=").append(ConfigureItinerary.API_KEY);
        Log.i("urlll", url.toString());
        return url.toString();
    }

    private void setSecondsOffset(StringBuilder url) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date firstDate = null;
        long diffInSec = 0;
        try {
            firstDate = sdf.parse("1970-01-01T00:00:00");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String tomorrowAsString = dateFormat.format(tomorrow);
            String tomorrowMorning = tomorrowAsString + "T09:00:00";
            Date tomorrowDate = sdf.parse(tomorrowMorning);
            diffInSec = (Math.abs(tomorrowDate.getTime() - firstDate.getTime())) / 1000;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        StringBuilder urlForTimeZone = new StringBuilder("https://maps.googleapis.com/maps/api/timezone/json?location=");
        urlForTimeZone.append(Double.toString(MainActivity.lat)).append("%2C").append(Double.toString(MainActivity.lng));
        urlForTimeZone.append("&timestamp=").append(Long.toString(diffInSec));
        urlForTimeZone.append("&key=").append(ConfigureItinerary.API_KEY);
        Log.i("url timezone", urlForTimeZone.toString());
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(urlForTimeZone.toString())
                .method("GET", null).build();
        int rawOffset = 0;
        int dstOffset = 0;
        try {
            Response response = client.newCall(request).execute();
            JSONObject timeZoneObject = new JSONObject(response.body().string());
            rawOffset = timeZoneObject.optInt("rawOffset");
            dstOffset = timeZoneObject.optInt("dstOffset");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        seconds = diffInSec - rawOffset - dstOffset;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_itinerary, container, false);

        if((((NavigationView) getActivity().findViewById(R.id.nav_view)).getMenu().findItem(R.id.myitineraries).isChecked())){
            root.findViewById(R.id.saveItineraryButton).setVisibility(View.GONE);
            root.findViewById(R.id.deleteItineraryButton).setVisibility(View.VISIBLE);
        }else{
            root.findViewById(R.id.saveItineraryButton).setVisibility(View.VISIBLE);
            root.findViewById(R.id.deleteItineraryButton).setVisibility(View.GONE);
        }

        getTransportInfo();
        setLatLng();
        mapView = (CustomMapView) root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        MapsInitializer.initialize(getActivity().getApplicationContext());
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googlemap) {

                googleMap = googlemap;
                for(int i=0; i<latLngs.size();i++) {
                    LatLng latLng = latLngs.get(i);
                    if(latLng!=null) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng).title(dayOrder.get(i).toUpperCase());
                        if( i==0){
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(latLngs.get(0)).zoom(13).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }else{
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        }
                        googleMap.addMarker(markerOptions);
                    }
                }

            }

        });

        int accommodationImage = getResources().getIdentifier("accommodation", "drawable", getContext().getPackageName());
        RecyclerView recyclerView = root.findViewById(R.id.attarctionItineraryRecyclerView);
        ItineraryAttraction_RecyclerViewAdapter adapter = new ItineraryAttraction_RecyclerViewAdapter(getContext(), dayOrder, daysInfo, attractions, departureTime, accommodationImage);
        recyclerView.setAdapter(adapter);
        setTheDaysVisibility();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return root;
    }

    private void setLatLng(){
        latLngs = new ArrayList<>();
        for(int i=0; i<locationsOrderAddress.size(); i++) {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + locationsOrderAddress.get(i) + "&key=" + ConfigureItinerary.API_KEY;
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                JSONObject addressObj = new JSONObject(response.body().string());
                JSONArray addressResults = addressObj.optJSONArray("results");
                if (addressResults != null && addressResults.optJSONObject(0) != null) {
                    JSONObject addressGeometry = addressResults.optJSONObject(0).optJSONObject("geometry");
                    if (addressGeometry != null) {
                        JSONObject location = addressGeometry.optJSONObject("location");
                        if (location != null) {
                            double lat = location.optDouble("lat");
                            double lng = location.optDouble("lng");
                            LatLng latLng = new LatLng(lat,lng);
                            latLngs.add(latLng);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    private void setTheDaysVisibility() {

        if( ! ((NavigationView) getActivity().findViewById(R.id.nav_view)).getMenu().findItem(R.id.myitineraries).isChecked() ) {
            getActivity().findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.linearLayout2).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.descriptionCityImage).setVisibility(View.GONE);
            getActivity().findViewById(R.id.descriptionCityTitle).setVisibility(View.GONE);
        }
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