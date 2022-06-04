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

public class ForgotPasswordActivity extends AppCompatActivity {

    String resetCode;
    String email;
    EditText code;
    EditText newPassword;
    EditText confirmPassword;
    int numberOfAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        numberOfAttempts=0;
        resetCode = getIntent().getStringExtra("code");
        email = getIntent().getStringExtra("email");
        code = findViewById(R.id.code);
        newPassword = findViewById(R.id.forgotNewPassword);
        confirmPassword = findViewById(R.id.forgotConfirmPassword);
    }

    public boolean verify() {

        if (code.getText().toString().trim().isEmpty()){
            code.requestFocus();
            code.setError("Enter the code");
            return false;
        }
        if (newPassword.getText().toString().trim().isEmpty()) {
            newPassword.requestFocus();
            newPassword.setError("Enter the password");
            return false;
        }
        if (confirmPassword.getText().toString().trim().isEmpty()) {
            confirmPassword.requestFocus();
            confirmPassword.setError("Enter the password");
            return false;
        }

        if (!newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.requestFocus();
            confirmPassword.setError("Enter the same new password");
            return false;
        }

        if (!code.getText().toString().equals(resetCode)) {
            numberOfAttempts++;
            if(numberOfAttempts==3){
                Toast.makeText(ForgotPasswordActivity.this, "3 times wrong code", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            code.requestFocus();
            code.setError("Wrong code");
            return false;
        }
        return true;
    }

    public void resetPassword(View view) {
        if (verify()) {
            class ResetPassword extends AsyncTask<String, String, String> {

                public ResetPassword() {

                }

                @Override
                protected void onPostExecute(String s) {
                    Toast.makeText(ForgotPasswordActivity.this, s, Toast.LENGTH_SHORT).show();
                    if(s.equals("Password changed")){
                        Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(ForgotPasswordActivity.this, EnterEmailActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                protected String doInBackground(String... voids) {

                    StringBuilder result = new StringBuilder();
                    String server = LoginActivity.server.concat("changePassword.php");
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
            ResetPassword resetPassword = new ResetPassword();
            resetPassword.execute(email, newPassword.getText().toString());

        }
    }


}