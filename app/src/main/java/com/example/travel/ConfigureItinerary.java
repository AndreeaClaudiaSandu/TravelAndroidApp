package com.example.travel;

import android.util.Log;

import com.google.common.collect.Sets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    static int final_res;

    public ConfigureItinerary() {
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

    public ArrayList<String> getOrder(String accommodationAddress) {
        setMatrix(accommodationAddress);

        int numberOfAttractions = addresses.size() - 1;
        int minAttractions = numberOfAttractions / numberOfDays;
        int nrOfDaysWithMoreAttractions = numberOfAttractions % numberOfDays;
        ArrayList<Integer> alreadyVisited = new ArrayList<>();
        ArrayList<String> orderByDay = new ArrayList<>();
        for (int j = 0; j < numberOfDays; j++) {
            Set<Integer> set = new HashSet<>();
            Set<Set<Integer>> combinations;
            for (int i = 0; i < numberOfAttractions; i++) {
                if (!alreadyVisited.contains(i)) {
                    set.add(i + 1);
                }
            }

            if (nrOfDaysWithMoreAttractions != 0 && j < nrOfDaysWithMoreAttractions) {
                combinations = Sets.combinations(set, minAttractions + 1);
            } else {
                combinations = Sets.combinations(set, minAttractions);
            }

            ArrayList<String> orderForADay = new ArrayList<>();
            ArrayList<Integer> maybeVisited = new ArrayList<>();
            orderForADay = getOrderForADay(combinations, orderForADay, maybeVisited);

            alreadyVisited.addAll(alreadyVisited.size(), maybeVisited);
            orderByDay.add("day" + (j + 1));
            orderByDay.addAll(orderByDay.size(), orderForADay);
        }

        return orderByDay;
    }

    private ArrayList<String> getOrderForADay(Set<Set<Integer>> combinations, ArrayList<String> firstOrder, ArrayList<Integer> maybeVisited) {
        int min = Integer.MAX_VALUE;
        for (Set<Integer> subset : combinations) {
            Log.i("now subset", subset.toString());
            int[][] subMatrix = generateSubMatrix(subset);

            Integer[] subsetOrder = new Integer[subset.size()];
            subset.toArray(subsetOrder);

            ArrayList<String> attractionsOrder = new ArrayList<>();
            ArrayList<Integer> order = new ArrayList<>();
//            int value = travelingSalesmanProblem(subMatrix, 0, order);
            final_res = Integer.MAX_VALUE;
            Log.i("final", Integer.toString(final_res));
            tsp(subMatrix, order);

            Log.i("final", Integer.toString(final_res));
            Log.i("final", order.toString());
//            if (value < min) {
//                min = value;
            if (final_res < min) {
                min = final_res;

                attractionsOrder.add("accommodation");
                maybeVisited.removeAll(maybeVisited);
                for (int i = 1; i < order.size() - 1; i++) {
                    maybeVisited.add(subsetOrder[order.get(i) - 1] - 1);
                    attractionsOrder.add(MainActivity.pickedAttractions.get(subsetOrder[order.get(i) - 1] - 1));
                }
                attractionsOrder.add("accommodation");
                firstOrder = attractionsOrder;

            }
        }
        return firstOrder;
    }

    private int[][] generateSubMatrix(Set<Integer> subset) {

        Integer[] array = subset.toArray(new Integer[subset.size()]);
        for (int i = 0; i < array.length; i++) {
            Log.i("now array", Integer.toString(array[i]));
        }

        int[][] subMatrix = new int[subset.size() + 1][subset.size() + 1];
        subMatrix[0][0] = 0;
        for (int i = 1; i < subMatrix.length; i++) {
            subMatrix[0][i] = distanceMatrix[0][array[i - 1]];
            subMatrix[i][0] = distanceMatrix[array[i - 1]][0];
        }
        for (int i = 1; i < subMatrix.length; i++) {
            for (int j = 1; j < subMatrix.length; j++) {
                subMatrix[i][j] = distanceMatrix[array[i - 1]][array[j - 1]];
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
        for (int i = 1; i < order.size() - 1; i++) {
            attractionsOrder.add(MainActivity.pickedAttractions.get(order.get(i) - 1));
        }
        attractionsOrder.add("accommodation");
        Log.i("line graph", order.toString());

        return attractionsOrder;
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
            address = address.replace(" ", "%20").replace(",", "%2C").replace("à", "%C3%A0").replace("Ã", "%C3%83").replace("ã", "%C3%A3");
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
                for (int i = 0; i < possibleSolution.size(); i++) {
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


    public static void tsp(int graph[][], ArrayList<Integer> order) {

        int curr_path[] = new int[graph.length];
        boolean visited[] = new boolean[graph.length];
        int min_path = Integer.MAX_VALUE;
        // Calculate initial lower bound for the root node
        // using the formula 1/2 * (sum of first min +
        // second min) for all edges.
        // Also initialize the curr_path and visited array
        int curr_bound = 0;
        Arrays.fill(curr_path, -1);
        Arrays.fill(visited, false);

        // Compute initial bound
        for (int i = 0; i < graph.length; i++)
            curr_bound += (firstMin(graph, i) +
                    secondMin(graph, i));

        // Rounding off the lower bound to an integer
        curr_bound = (curr_bound == 1) ? curr_bound / 2 + 1 :
                curr_bound / 2;

        // We start at vertex 1 so the first vertex
        // in curr_path[] is 0
        visited[0] = true;
        curr_path[0] = 0;

        // Call to TSPRec for curr_weight equal to
        // 0 and level 1
        TSPRec(graph, curr_bound, 0, 1, curr_path, order, visited);

    }

    // Function to find the minimum edge cost
    // having an end at the vertex i
    static int firstMin(int graph[][], int i) {
        int min = Integer.MAX_VALUE;
        for (int k = 0; k < graph.length; k++)
            if (graph[i][k] < min && i != k)
                min = graph[i][k];
        return min;
    }

    // function to find the second minimum edge cost
    // having an end at the vertex i
    static int secondMin(int graph[][], int i) {
        int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
        for (int j = 0; j < graph.length; j++) {
            if (i == j)
                continue;

            if (graph[i][j] <= first) {
                second = first;
                first = graph[i][j];
            } else if (graph[i][j] <= second &&
                    graph[i][j] != first)
                second = graph[i][j];
        }
        return second;
    }

    // function that takes as arguments
    // curr_bound -> lower bound of the root node
    // curr_weight-> stores the weight of the path so far
    // level-> current level while moving in the search
    //         space tree
    // curr_path[] -> where the solution is being stored which
    //             would later be copied to final_path[]
    static void TSPRec(int graph[][], int curr_bound, int curr_weight,
                       int level, int curr_path[], ArrayList<Integer> order, boolean[] visited) {
        // base case is when we have reached level N which
        // means we have covered all the nodes once
        if (level == graph.length) {
            // check if there is an edge from last vertex in
            // path back to the first vertex
            if (graph[curr_path[level - 1]][curr_path[0]] != 0) {
                // curr_res has the total weight of the
                // solution we got
                int curr_res = curr_weight +
                        graph[curr_path[level - 1]][curr_path[0]];

                // Update final result and final path if
                // current result is better.
                if (curr_res < final_res) {
                    order.removeAll(order);
                    for (int i = 0; i < curr_path.length; i++) {
                        order.add(curr_path[i]);
                    }
                    order.add(curr_path[0]);

                    final_res = curr_res;

                }
            }
            return;
        }

        // for any other level iterate for all vertices to
        // build the search space tree recursively
        for (int i = 0; i < graph.length; i++) {
            // Consider next vertex if it is not same (diagonal
            // entry in adjacency matrix and not visited
            // already)
            if (graph[curr_path[level - 1]][i] != 0 &&
                    visited[i] == false) {
                int temp = curr_bound;
                curr_weight += graph[curr_path[level - 1]][i];

                // different computation of curr_bound for
                // level 2 from the other levels
                if (level == 1)
                    curr_bound -= ((firstMin(graph, curr_path[level - 1]) +
                            firstMin(graph, i)) / 2);
                else
                    curr_bound -= ((secondMin(graph, curr_path[level - 1]) +
                            firstMin(graph, i)) / 2);

                // curr_bound + curr_weight is the actual lower bound
                // for the node that we have arrived on
                // If current lower bound < final_res, we need to explore
                // the node further
                if (curr_bound + curr_weight < final_res) {
                    curr_path[level] = i;
                    visited[i] = true;

                    // call TSPRec for the next level
                    TSPRec(graph, curr_bound, curr_weight, level + 1,
                            curr_path, order, visited);
                }

                // Else we have to prune the node by resetting
                // all changes to curr_weight and curr_bound
                curr_weight -= graph[curr_path[level - 1]][i];
                curr_bound = temp;
                // Also reset the visited array
                Arrays.fill(visited, false);
                for (int j = 0; j <= level - 1; j++)
                    visited[curr_path[j]] = true;
            }
        }
    }
}
