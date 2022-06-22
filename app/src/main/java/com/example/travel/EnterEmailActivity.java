package com.example.travel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnterEmailActivity extends AppCompatActivity {

    private String forgotPasswordCode;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_email);
        email = findViewById(R.id.email);
    }

    public void verifyAccountAndSendCode(View view){

        if(!email.getText().toString().trim().isEmpty()){

            class VerifyExistingAccount extends AsyncTask<String, String, String> {

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if(s.equals("An account with this email already exists")){
                        sendResetPasswordCode();
                    }
                    else{
                        Toast.makeText(EnterEmailActivity.this, s, Toast.LENGTH_SHORT).show();
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
        else{
            email.requestFocus();
            email.setError("Enter the email");
        }
    }

    private void sendResetPasswordCode() {
        final String username = "exploretheworldnow.app@gmail.com";
        final String password = "thgiqlvzhvdwpeni";
        forgotPasswordCode = RegisterActivity.generateCode();
        String messageToSend = "Your code for resetting the password is: " + forgotPasswordCode + ".";
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
            message.setSubject("Reset password code");
            message.setText(messageToSend);
            Transport.send(message);
            Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        goToResetPasswordActivity();
    }

    private void goToResetPasswordActivity() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        intent.putExtra("code", forgotPasswordCode);
        intent.putExtra("email", email.getText().toString());
        startActivity(intent);
    }
}