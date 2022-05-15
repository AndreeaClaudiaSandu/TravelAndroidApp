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

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentActivities extends Fragment {

    ArrayList<Activity> activities = new ArrayList<>();

    public FragmentActivities() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivities();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }


    private void setActivities() {

        {

            class GetActivities extends AsyncTask<String, String, String> implements Activities_RecyclerViewAdapter.ItemClickListener{

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if (s.equals("Connection error. Try again.") || s.equals("No activities")) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                    } else{
                        RecyclerView recyclerView = getView().findViewById(R.id.activitiesRecyclerView);
                        Activities_RecyclerViewAdapter adapter = new Activities_RecyclerViewAdapter(getContext(), activities, this );
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                    }
                }

                @Override
                protected String doInBackground(String... strings) {

                    String server = LoginActivity.server.concat("getActivities.php");
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
                            String[] fields=  line.split("=,");

                            Log.i("pos0: ", fields[0]);
                            Log.i("pos1: ", fields[1]);
                            Log.i("pos2: ", fields[2]);
                            Log.i("pos3: ", fields[3]);
                            Log.i("pos4: ", fields[4]);
                            String tara = fields[4].substring(0,1).toUpperCase() + fields[4].substring(1);
                            String oras = fields[3].substring(0,1).toUpperCase() + fields[3].substring(1);
                            activities.add(new Activity(fields[0],fields[1], fields[2], oras,tara,getResources().getIdentifier(fields[0].replace(" ", "").toLowerCase(), "drawable", getContext().getPackageName())));
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
                    public void onItemClick(Activity activity) {
                    Toast.makeText(getContext(), activity.getDenumire(), Toast.LENGTH_SHORT).show();
                    Fragment fragment = FragmentActivity.newInstance(activity);
                    ((MainActivity) getActivity()).replaceFragment(fragment);
//                    Fragment fragment = FragmentCity.newInstance(cities, city.getName());
//                    ((MainActivity) getActivity()).replaceFragment(fragment);
                }
            }

            GetActivities activities = new GetActivities();
            activities.execute();
        }

    }


}
