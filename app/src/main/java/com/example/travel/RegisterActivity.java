package com.example.travel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RegisterActivity extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmationPassword;
    EditText country;
    EditText city;
    EditText birthDate;
    public static String activationCode;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmationPassword = findViewById(R.id.password_confirmation);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        birthDate = findViewById(R.id.birth_date);

        birthDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DATE);
            DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, R.style.DatePicker, (view, year, month, dayOfMonth) -> birthDate.setText(year+"-"+month+"-"+dayOfMonth), year,month,day);
            datePickerDialog.show();
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.loginDarkBlue));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.loginDarkBlue));

        });
    }


    public void sendMail(View view){

        if(checkValidation()) {

            class VerifyExistingAccount extends AsyncTask<String, String, String> {

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    switch (s) {
                        case "An account with this email already exists":
                            Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            break;
                        case "Not an existing account":
                            sendCode();
                            break;
                        case "Not connected":
                            Toast.makeText(RegisterActivity.this, "Connection error. Try again.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                @Override
                protected String doInBackground(String... strings) {

                    String server = LoginActivity.server.concat("verifyExistingAccount.php");
                    StringBuilder result = new StringBuilder();
                    try {
                        URL url = new URL(server);
                        HttpURLConnection http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("POST");
                        http.setDoInput(true);
                        http.setDoOutput(true);

                        OutputStream output = http.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                        Uri.Builder builder = new Uri.Builder().appendQueryParameter("email", strings[0]);
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

            VerifyExistingAccount verifyAccount = new VerifyExistingAccount();
            verifyAccount.execute(email.getText().toString());





        }
    }

    private void sendCode() {
        final String username = "exploretheworldnow.app@gmail.com";
        final String password = "parola1234P";
        activationCode = generateCode();
        String messageToSend = "Your activation code is: " + activationCode + ".";
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "578");
        Session session = Session.getInstance(properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getText().toString()));
            message.setSubject("Activation code");
            message.setText(messageToSend);
            Transport.send(message);
            Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        goToActivateAccountActivity();
    }

    public void goToActivateAccountActivity(){
        Intent intent = new Intent(this, ActivateAccountActivity.class);
        intent.putExtra("code", activationCode);
        intent.putExtra("email", email.getText().toString());
        intent.putExtra("password", password.getText().toString());
        intent.putExtra("firstName", firstName.getText().toString());
        intent.putExtra("lastName", lastName.getText().toString());
        intent.putExtra("country", country.getText().toString());
        intent.putExtra("city", city.getText().toString());
        intent.putExtra("birthDate", birthDate.getText().toString());
        startActivity(intent);
    }

    public String generateCode(){
        String string = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder code = new StringBuilder(7);
        for(int i=0;i<7;i++){
            int index = (int) (string.length()*Math.random());
            code.append(string.charAt(index));
        }
        return code.toString();
    }

    public boolean checkValidation(){
        if(firstName.getText().toString().trim().length()<=0){
            firstName.requestFocus();
            firstName.setError("Enter first name");
            return false;
        }
        if(lastName.getText().toString().trim().length()<=0){
            lastName.requestFocus();
            lastName.setError("Enter last name");
            return false;
        }
        if(email.getText().toString().trim().length()<=0){
            email.requestFocus();
            email.setError("Enter last name");
            return false;
        }
        if(password.getText().toString().trim().length()<=0){
            password.requestFocus();
            password.setError("Enter last name");
            return false;
        }
        if(confirmationPassword.getText().toString().trim().length()<=0){
            confirmationPassword.requestFocus();
            confirmationPassword.setError("Enter last name");
            return false;
        }
        if(!password.getText().toString().trim().equals(confirmationPassword.getText().toString().trim())){
            confirmationPassword.requestFocus();
            confirmationPassword.setError("Different password");
            return false;
        }
        return true;
    }
}