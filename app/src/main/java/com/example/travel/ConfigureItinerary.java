package com.example.travel;

import android.util.Log;

import com.google.common.collect.Sets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConfigureItinerary {

    static ArrayList<String> addresses;
    static final String API_KEY = "AIzaSyBU9zEvhGTMNA42QzdZIo0Z3gzn-5WmIjQ";
    int[][] distanceMatrix;
    int numberOfDays;

    public ConfigureItinerary(){
        numberOfDays = MainActivity.numberOfDays;
    }

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

    public ArrayList<String> getOrder(String accommodationAddress){
        setMatrix(accommodationAddress);

        int numberOfAttractions = addresses.size()-1;
        int minAttractions = numberOfAttractions/numberOfDays;
        int nrOfDaysWithMoreAttractions =  numberOfAttractions%numberOfDays;
        ArrayList<Integer> alreadyVisited = new ArrayList<>();
        ArrayList<String> orderByDay = new ArrayList<>();
        for(int j=0; j<numberOfDays; j++) {
            Set<Integer> set = new HashSet<>();
            Set<Set<Integer>> combinations;
            for (int i = 0; i < numberOfAttractions; i++) {
                if(!alreadyVisited.contains(i)) {
                    set.add(i + 1);
                }
            }

            if (nrOfDaysWithMoreAttractions != 0 && j<nrOfDaysWithMoreAttractions) {
                combinations = Sets.combinations(set, minAttractions + 1);
            } else {
                combinations = Sets.combinations(set, minAttractions);
            }

            ArrayList<String> orderForADay = new ArrayList<>();
            ArrayList<Integer> maybeVisited = new ArrayList<>();
            orderForADay = getOrderForADay(combinations, orderForADay, maybeVisited);

            alreadyVisited.addAll(alreadyVisited.size(), maybeVisited);
            orderByDay.add("day" + (j+1));
            orderByDay.addAll(orderByDay.size(),orderForADay);
        }

        return orderByDay;
    }

    private ArrayList<String> getOrderForADay(Set<Set<Integer>> combinations, ArrayList<String> firstOrder, ArrayList<Integer> maybeVisited) {
        int min = Integer.MAX_VALUE;
        for( Set<Integer> subset: combinations){
            Log.i("now subset", subset.toString());
            int[][] subMatrix = generateSubMatrix(subset);

            Integer[] subsetOrder = new Integer[subset.size()];
            subset.toArray(subsetOrder);

            ArrayList<String> attractionsOrder = new ArrayList<>();
            ArrayList<Integer> order = new ArrayList<>();
            int value = travelingSalesmanProblem(subMatrix, 0, order);
            if(value < min) {
                min = value;

                attractionsOrder.add("accommodation");
                maybeVisited.removeAll(maybeVisited);
                for(int i=1 ;i<order.size()-1;i++){
                    maybeVisited.add(subsetOrder[order.get(i) - 1]-1);
                    attractionsOrder.add(MainActivity.pickedAttractions.get(subsetOrder[order.get(i) - 1]-1));
                }
                attractionsOrder.add("accommodation");
                firstOrder = attractionsOrder;

            }
        }
        return firstOrder;
    }

    private int[][] generateSubMatrix(Set<Integer> subset) {

        Integer[]  array = subset.toArray(new Integer[subset.size()]);
        for(int i=0; i<array.length; i++){
            Log.i("now array", Integer.toString( array[i]));
        }

        int[][] subMatrix = new int[subset.size()+1][subset.size()+1];
        subMatrix[0][0] = 0;
        for(int i=1;i<subMatrix.length;i++){
            subMatrix[0][i] = distanceMatrix[0][array[i-1]];
            subMatrix[i][0] = distanceMatrix[array[i-1]][0];
        }
        for(int i=1; i<subMatrix.length;i++){
            for(int j=1; j<subMatrix.length; j++){
                subMatrix[i][j]=distanceMatrix[array[i-1]][array[j-1]];
            }
        }

        return subMatrix;
//        //afisare matrice
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < distanceMatrix.length; i++) {
//            builder = new StringBuilder();
//            for (int j = 0; j < distanceMatrix[i].length; j++) {
//                builder.append(distanceMatrix[i][j]).append(" ");
//            }
//            Log.i("now matrix", builder.toString());
//        }
//        //afisare submatrice
//        builder = new StringBuilder();
//        for (int i = 0; i < subMatrix.length; i++) {
//            builder = new StringBuilder();
//            for (int j = 0; j < subMatrix[i].length; j++) {
//                builder.append(subMatrix[i][j]).append(" ");
//            }
//            Log.i("now line", builder.toString());
//        }

    }

    private void setMatrix(String accommodationAddress) {
        JSONObject jsonObject = getJsonMatrix(accommodationAddress);
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
    }

    public ArrayList<String> getAttractionsOrder(String accommodationAddress) {
        setMatrix(accommodationAddress);

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < distanceMatrix.length; i++) {
            builder = new StringBuilder();
            for (int j = 0; j < distanceMatrix[i].length; j++) {
                builder.append(distanceMatrix[i][j]).append(" ");
            }
            Log.i("line", builder.toString());
        }

        ArrayList<String> attractionsOrder = new ArrayList<>();
        ArrayList<Integer> order = new ArrayList<>();
        travelingSalesmanProblem(distanceMatrix, 0, order);
        attractionsOrder.add("accommodation");
        for(int i=1 ;i<order.size()-1;i++){
            attractionsOrder.add(MainActivity.pickedAttractions.get(order.get(i) - 1));
        }
        attractionsOrder.add("accommodation");
        Log.i("line graph", order.toString());

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
            address = address.replace(" ", "%20").replace(",", "%2C").replace("à", "%C3%A0").replace("Ã","%C3%83").replace("ã", "%C3%A3");
            addressesString.append(address).append("%7C");
        }

        url.append(addressesString.toString()).append("&destinations=").append(addressesString.toString());
        url.append("&mode=walking");
        url.append("&key=").append(API_KEY);
        Log.i("url matrix", url.toString());
        return url.toString();
    }

    static int travelingSalesmanProblem(int graph[][], int s, ArrayList<Integer> order) {
        ArrayList<Integer> vertex = new ArrayList<Integer>();

//        ArrayList<Integer> solution = new ArrayList<>();

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
                order.removeAll(order);
                for(int i=0;i<possibleSolution.size();i++){
                    order.add(possibleSolution.get(i));
                }
            }
            min_path = Math.min(min_path, current_pathweight);

        } while (findNextPermutation(vertex));

        Log.i("min", "" + min_path);
        return min_path;
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
