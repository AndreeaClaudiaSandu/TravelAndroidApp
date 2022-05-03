package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;

import java.net.HttpCookie;

public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    public static String server = "http://192.168.0.101/travel/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.enableDefaults();



        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
    }

    public void goToRegisterPage(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view){
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if(checkValidation(email, password)) {
            Background background = new Background(this);
            background.execute(email, password);
        }
    }

    public boolean checkValidation(String email, String password){
        if(email.length()<=0){
            emailField.requestFocus();
            emailField.setError("Enter email");
            return false;
        }
        if(password.length()<=0){
            passwordField.requestFocus();
            passwordField.setError("Enter password");
            return false;
        }
        return true;
    }

}