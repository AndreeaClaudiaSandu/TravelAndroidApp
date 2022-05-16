package com.example.travel;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Locale;
import java.util.Objects;

public class FragmentCityTransport extends Fragment {

    ArrayList<ExternalTransport> externalTransport = new ArrayList<>();
    String cityName;

    public FragmentCityTransport() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FragmentCityTransport newInstance(String cityName) {
        FragmentCityTransport fragment = new FragmentCityTransport();
        fragment.cityName = cityName;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setExternalTransport();
        setInternalTransport();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_transport, container, false);
    }

    private void setExternalTransport() {

        class GetExternalTransport extends AsyncTask<String, String, String> {

            @Override
            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
                if (s.equals("Connection error. Try again.") || s.equals("No external transport")) {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                } else {

                    RecyclerView recyclerView = getView().findViewById(R.id.externalTransportRecyclerView);
                    ExternalTransport_RecyclerViewAdapter adapter = new ExternalTransport_RecyclerViewAdapter(getContext(), externalTransport);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                }
            }

            @Override
            protected String doInBackground(String... strings) {

                String server = LoginActivity.server.concat("externalTransport.php");
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
                        Log.i("line", line);
                        result.append(line);
                        String[] fields = line.split("=,");
                        if (fields.length > 1) {
                            externalTransport.add(new ExternalTransport(cityName, fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7], (fields.length == 9) ? fields[8] : ""));
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

        GetExternalTransport externalTransports = new GetExternalTransport();
        Log.i("city", cityName);
        externalTransports.execute(cityName);
    }

    private void setInternalTransport() {
    }

}