package com.example.travel;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConfigureItinerary {

    static ArrayList<String> addresses;
    static final String API_KEY = "AIzaSyBU9zEvhGTMNA42QzdZIo0Z3gzn-5WmIjQ";
    int[][] distanceMatrix;

    public void setAddresses(String accommodationAddress) {
        addresses = new ArrayList<>();
        ArrayList<String> attractionsName = MainActivity.pickedAttractions;
        ArrayList<Attraction> attractions = FragmentCityConfigureItinerary.attractions;
        addresses.add(accommodationAddress);
        for (int i = 0; i < attractionsName.size(); i++) {
            for (int j = 0; j < attractions.size(); j++) {
                if (attractionsName.get(i).equals(attractions.get(j).getName())) {
                    addresses.add(attractions.get(j).location);
                }
            }
        }
    }

    public ArrayList<String> getAttractionsOrder(String accommodationAddress) {
        JSONObject jsonObject = getJsonMatrix(accommodationAddress);
//
//        if (!jsonObject.optString("status").equals("OK")) {
//            return "Not a valid address";
//        }

        distanceMatrix = new int[addresses.size()][addresses.size()];

        JSONArray rowsArray = jsonObject.optJSONArray("rows");
        for (int i = 0; i < rowsArray.length(); i++) {
            JSONObject firstElements = rowsArray.optJSONObject(i);
            JSONArray elementsArray = firstElements.optJSONArray("elements");
            for (int j = 0; j < elementsArray.length(); j++) {
                JSONObject elementObject = elementsArray.optJSONObject(j);
                JSONObject distanceObject = elementObject.optJSONObject("distance");
                int value = distanceObject.optInt("value");
                distanceMatrix[i][j] = value;
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < distanceMatrix.length; i++) {
            builder = new StringBuilder();
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                builder.append(distanceMatrix[i][j]).append(" ");
            }
            Log.i("line", builder.toString());
        }

        ArrayList<String> attractionsOrder = new ArrayList<>();
        ArrayList<Integer> order = travelingSalesmanProblem(distanceMatrix, 0);
        attractionsOrder.add("accommodation");
        for(int i=1 ;i<order.size()-1;i++){
            attractionsOrder.add(MainActivity.pickedAttractions.get(order.get(i) - 1));
        }
        attractionsOrder.add("accommodation");
        Log.i("line graph", travelingSalesmanProblem(distanceMatrix, 0).toString());

        return  attractionsOrder;
    }

    public JSONObject getJsonMatrix(String accommodationAddress) {

        setAddresses(accommodationAddress);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String url = generateURL(addresses);
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject object = new JSONObject(response.body().string());
            Log.i("object", object.toString());
            return object;
        } catch (IOException | JSONException e) {
            Log.e("errorObj", e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public String generateURL(ArrayList<String> addresses) {

        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/distancematrix/json?");
        url.append("origins=");

        StringBuilder addressesString = new StringBuilder();
        for (int i = 0; i < addresses.size(); i++) {
            String address = addresses.get(i);
            address.replace(" ", "%20").replace(",", "%2C").replace("à", "%C3%A0");
            addressesString.append(address).append("%7C");
        }

        url.append(addressesString.toString()).append("&destinations=").append(addressesString.toString());
        url.append("&mode=walking");
        url.append("&key=").append(API_KEY);

        return url.toString();
    }

    static ArrayList<Integer> travelingSalesmanProblem(int graph[][], int s) {
        ArrayList<Integer> vertex = new ArrayList<Integer>();

        ArrayList<Integer> solution = new ArrayList<>();

        for (int i = 0; i < graph.length; i++)
            if (i != s)
                vertex.add(i);

        int min_path = Integer.MAX_VALUE;
        do {
            ArrayList<Integer> possibleSolution = new ArrayList<>();
            int current_pathweight = 0;
            int k = s;

            possibleSolution.add(k);

            for (int i = 0; i < vertex.size(); i++) {
                current_pathweight += graph[k][vertex.get(i)];
                k = vertex.get(i);
                possibleSolution.add(k);
            }

            possibleSolution.add(s);
            current_pathweight += graph[k][s];

            if (Math.min(min_path, current_pathweight) == current_pathweight) {
                solution = possibleSolution;
            }
            min_path = Math.min(min_path, current_pathweight);

        } while (findNextPermutation(vertex));

        Log.i("min", "" + min_path);
        return solution;
    }

    public static ArrayList<Integer> swap(ArrayList<Integer> data, int left, int right) {
        int temp = data.get(left);
        data.set(left, data.get(right));
        data.set(right, temp);
        return data;
    }

    public static ArrayList<Integer> reverse(ArrayList<Integer> data, int left, int right) {
        while (left < right) {
            int temp = data.get(left);
            data.set(left++, data.get(right));
            data.set(right--, temp);
        }
        return data;
    }

    public static boolean findNextPermutation(ArrayList<Integer> data) {
        if (data.size() <= 1)
            return false;

        int last = data.size() - 2;
        while (last >= 0) {
            if (data.get(last) < data.get(last + 1)) {
                break;
            }
            last--;
        }
        if (last < 0)
            return false;

        int nextGreater = data.size() - 1;
        for (int i = data.size() - 1; i > last; i--) {
            if (data.get(i) > data.get(last)) {
                nextGreater = i;
                break;
            }
        }
        data = swap(data, nextGreater, last);
        data = reverse(data, last + 1, data.size() - 1);
        return true;
    }


}
