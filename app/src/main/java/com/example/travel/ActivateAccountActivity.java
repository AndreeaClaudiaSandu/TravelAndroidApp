package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class ActivateAccountActivity extends AppCompatActivity {

    EditText editText;
    String code;
    String email;
    String password;
    String firstName;
    String lastName;
    String country;
    String city;
    String birthDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_account);
        editText = findViewById(R.id.activationCode);
        code = getIntent().getStringExtra("code");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        country = getIntent().getStringExtra("country");
        city = getIntent().getStringExtra("city");
        birthDate = getIntent().getStringExtra("birthDate");
        Toast.makeText(this, code, Toast.LENGTH_SHORT).show();
    }

    public void verifyCode(View view) {
//        Toast.makeText(this, editText.getText(), Toast.LENGTH_LONG);
        if (editText.getText().toString().equals(code)) {

            class CreateAccount extends AsyncTask<String, String, String> {

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(ActivateAccountActivity.this, s, Toast.LENGTH_SHORT).show();
                    if (s.equals("Account successfully created")) {
                        Intent intent = new Intent(ActivateAccountActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                protected String doInBackground(String... strings) {

                    String server = LoginActivity.server.concat("register.php");
                    StringBuilder result = new StringBuilder();
                    try {
                        URL url = new URL(server);
                        HttpURLConnection http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("POST");
                        http.setDoInput(true);
                        http.setDoOutput(true);

                        OutputStream output = http.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                        Uri.Builder builder = new Uri.Builder().appendQueryParameter("email", strings[0]).appendQueryParameter("password", strings[1]).appendQueryParameter("firstName", strings[2]).appendQueryParameter("lastName", strings[3]).appendQueryParameter("country", strings[4]).appendQueryParameter("city", strings[5]).appendQueryParameter("birthDate", strings[6]);

                        String data = builder.build().getEncodedQuery();
                        writer.write(data);
                        writer.flush();
                        writer.close();
                        output.close();

                        InputStream input = http.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.ISO_8859_1));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        reader.close();
                        input.close();
                        http.disconnect();
                        return result.toString();

                    } catch (
                            IOException e) {
                        e.printStackTrace();
                        result = new StringBuilder(Objects.requireNonNull(e.getMessage()));
                    }


                    return result.toString();
                }
            }

            CreateAccount createAccount = new CreateAccount();
            createAccount.execute(email, password, firstName, lastName, country, city, birthDate);

        } else {
            Toast.makeText(view.getContext(), "wrong code", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
        }
    }
}