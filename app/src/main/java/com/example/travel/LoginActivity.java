package com.example.travel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
//    public static String server = "http://192.168.0.101/travel/";

//    public static String server = "http://172.20.10.2/travel/";

    public static String server = "http://192.168.0.102/travel/";

    public static String connectedAccount;
    public static int idAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.enableDefaults();


        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
    }

    public void goToRegisterPage(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (checkValidation(email, password)) {

            class Background extends AsyncTask<String, String, String> {
                Context context;

                public Background(Context context) {
                    this.context = context;
                }

                @Override
                protected void onPostExecute(String s) {
                    if (!s.equals("Wrong password") && !s.equals("Not a valid account") && !s.equals("Connection error. Try again.")) {
                        connectedAccount = email;
                        Intent intent = new Intent();
                        intent.setClass(context, MainActivity.class);
                        context.startActivity(intent);
                    }
                    else{
                        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                protected String doInBackground(String... voids) {

                    StringBuilder result = new StringBuilder();
                    String server = LoginActivity.server.concat("login.php");
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
                        String line;
                        while ((line = reader.readLine()) != null) {
                            idAccount = Integer.valueOf(line);
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
            Background background = new Background(this);
            background.execute(email, password);
        }
    }

    public boolean checkValidation(String email, String password) {
        if (email.length() <= 0) {
            emailField.requestFocus();
            emailField.setError("Enter email");
            return false;
        }
        if (password.length() <= 0) {
            passwordField.requestFocus();
            passwordField.setError("Enter password");
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    public void goToEnterEmailActivity(View view) {
        Intent intent = new Intent(this, EnterEmailActivity.class);
        startActivity(intent);
    }

}