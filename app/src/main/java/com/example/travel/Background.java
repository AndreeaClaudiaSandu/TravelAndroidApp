package com.example.travel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.Objects;

public class Background extends AsyncTask<String, String, String>{
    Context context;

    public Background(Context context){
        this.context=context;
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        if(s.equals("Login successfully")) {
            Intent intent = new Intent();
            intent.setClass(context,MainActivity2.class);
            context.startActivity(intent);
        }

    }

    @Override
    protected String doInBackground(String... voids) {

        StringBuilder result= new StringBuilder();
        String server = "http://192.168.0.101/travel/login.php";
        try {
            URL url = new URL(server);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);

            OutputStream output = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

            Uri.Builder builder = new Uri.Builder().appendQueryParameter("email", voids[0]).appendQueryParameter("password", voids[1]);

            String data = builder.build().getEncodedQuery();
            writer.write(data);
            writer.flush();
            writer.close();
            output.close();

            InputStream input = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.ISO_8859_1));
            String line = "";
            while ((line=reader.readLine()) != null){
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
