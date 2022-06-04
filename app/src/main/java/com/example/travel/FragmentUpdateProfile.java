package com.example.travel;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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

public class FragmentUpdateProfile extends Fragment {

    String email;
    Account account = new Account();
    int year,month,day;


    public FragmentUpdateProfile() {
        // Required empty public constructor
    }

    public static FragmentUpdateProfile newInstance(String email) {
        FragmentUpdateProfile fragment = new FragmentUpdateProfile();
        fragment.email = email;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAccount();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_profile, container, false);
    }

    private void setAccount() {

        class GetAccount extends AsyncTask<String, String, String>{

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("Not an account") || s.equals("Connection error. Try again.")) {
                    Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                } else {

                    EditText birthDate = getActivity().findViewById(R.id.birth_date);

                    birthDate.setOnClickListener(v -> {
                        final Calendar calendar = Calendar.getInstance();
                        year = calendar.get(Calendar.YEAR);
                        month = calendar.get(Calendar.MONTH);
                        day = calendar.get(Calendar.DATE);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DatePicker, (view, year, month, dayOfMonth) -> birthDate.setText(year+"-"+ Integer.toString( month+1)+"-"+dayOfMonth), year,month,day);
                        datePickerDialog.show();
                        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.loginDarkBlue));
                        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.loginDarkBlue));

                    });

                    if(account.getFirstName()!=null){
                        ((EditText) getActivity().findViewById(R.id.firstName)).setText(account.getFirstName());
                    }
                    if(account.getLastName()!=null){
                        ((EditText) getActivity().findViewById(R.id.lastName)).setText(account.getLastName());
                    }
                    if(account.getCountry()!=null){
                        ((EditText) getActivity().findViewById(R.id.country)).setText(account.getCountry());
                    }
                    if(account.getCity()!=null){
                        ((EditText) getActivity().findViewById(R.id.city)).setText(account.getCity());
                    }
                    if(account.getBirthDate()!=null){
                        ((EditText) getActivity().findViewById(R.id.birth_date)).setText(account.getBirthDate());
                    }
                }
            }

            @Override
            protected String doInBackground(String... strings) {

                String server = LoginActivity.server.concat("getAccount.php");
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
                        Log.i("line", line);
                        if(line.contains("prenume: ")){
                            account.setFirstName(line.substring(9));
                        }
                        if(line.contains("nume: ")){
                            account.setLastName(line.substring(6));
                        }
                        if(line.contains("tara: ")){
                            account.setCountry(line.substring(6));
                        }
                        if(line.contains("oras: ")){
                            account.setCity(line.substring(6));
                        }
                        if(line.contains("dataNasterii: ")){
                            account.setBirthDate(line.substring(14));
                        }
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

        GetAccount account = new GetAccount();
        account.execute(email);
    }


}