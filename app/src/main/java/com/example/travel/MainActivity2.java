package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity2 extends AppCompatActivity {

    ArrayList<City> cities = new ArrayList<>();
    int[] citiesImages = {R.drawable.barcelona,R.drawable.roma2,R.drawable.roma3,
            R.drawable.barcelona2,R.drawable.barcelona,R.drawable.barcelona,
            R.drawable.barcelona,R.drawable.barcelona,R.drawable.barcelona, R.drawable.roma3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setCities();
    }

    private void setCities(){
//        String[] citiesName = getResources().getStringArray(R.array.cities);
//        for(int i=0; i<citiesName.length; i++){
//            cities.add(new City(citiesName[i], citiesImages[i]));
//        }

        class GetCities extends AsyncTask<String, String, String> {

            ArrayList<String> citiesNameDatabase = new ArrayList<>();

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("Connection error. Try again.")) {
                    Toast.makeText(MainActivity2.this, s, Toast.LENGTH_SHORT).show();
                }else{
                    for (int i=0; i<citiesNameDatabase.size();i++){
                        cities.add(new City(citiesNameDatabase.get(i), getResources().getIdentifier(citiesNameDatabase.get(i), "drawable", getPackageName())));
                    }
                    RecyclerView recyclerView = findViewById(R.id.citiesRecyclerView);
                    Cities_RecyclerViewAdapter adapter = new Cities_RecyclerViewAdapter(MainActivity2.this, cities);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));

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
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        citiesNameDatabase.add(line);
                    }
                    reader.close();
                    input.close();
                    http.disconnect();
                    return citiesNameDatabase.toString();

                } catch (
                        IOException e) {
                    e.printStackTrace();
                    return ( new StringBuilder(Objects.requireNonNull(e.getMessage()))).toString();
                }

            }
        }

        GetCities cities = new GetCities();
        cities.execute();
    }
}