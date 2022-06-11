package com.example.travel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Display display;
    EditText firstName, lastName, country, city, birthDate, password, newPassword, confirmPassword;
    public static ArrayList<String> pickedAttractions;
    public static String accommodationAddress;
    public static int numberOfDays;
    public static double lat, lng;
    int cityId;
    int itineraryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickedAttractions = new ArrayList<>();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

//        setFrameLayoutHeight();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        toolbar.setTitle("Pick a city");
        replaceFragment(new FragmentPickCity());

    }

    private void setFrameLayoutHeight() {
        display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        float density = getResources().getDisplayMetrics().density;
        int dpHeight = (int) (metrics.heightPixels / density);
        int frameHeightdp = dpHeight - 90;
        int frameHeightPixels = (int) (frameHeightdp * density);
        FrameLayout layout = (FrameLayout) findViewById(R.id.frameLayout);
        ViewGroup.LayoutParams parms = (ViewGroup.LayoutParams) layout.getLayoutParams();
        parms.height = frameHeightPixels;
        layout.setLayoutParams(parms);
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Fragment previousFragment = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
            String previousDay = null;
            if (previousFragment instanceof FragmentItinerary) {
                previousDay = ((FragmentItinerary) previousFragment).dayNumber;
            }
            getSupportFragmentManager().popBackStackImmediate();
            if (getSupportFragmentManager().getFragments().size() > 0) {
                Fragment currentFragment = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
                if (currentFragment instanceof FragmentActivities || currentFragment instanceof FragmentActivity) {
                    ((Toolbar) findViewById(R.id.toolbar)).setTitle(getResources().getString(R.string.experiences));
                    navigationView.getMenu().findItem(R.id.experiences).setChecked(true);
                } else {
                    ((Toolbar) findViewById(R.id.toolbar)).setTitle(getResources().getString(R.string.pick_a_city));
                    navigationView.getMenu().findItem(R.id.pick_city).setChecked(true);
                }

                if (currentFragment instanceof FragmentCityDescription || currentFragment instanceof FragmentCityTransport) {
                    if (findViewById(R.id.descriptionCityImage) != null) {
                        findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
                        findViewById(R.id.linearLayout2).setVisibility(View.VISIBLE);
                        findViewById(R.id.descriptionCityImage).setVisibility(View.VISIBLE);
                        findViewById(R.id.descriptionCityTitle).setVisibility(View.VISIBLE);
                    }
                } else if (currentFragment instanceof FragmentCityAttractions || currentFragment instanceof FragmentCityAttraction || currentFragment instanceof FragmentCityConfigureItinerary || currentFragment instanceof FragmentItinerary) {
                    findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.linearLayout2).setVisibility(View.VISIBLE);
                    findViewById(R.id.descriptionCityImage).setVisibility(View.GONE);
                    findViewById(R.id.descriptionCityTitle).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.linearLayout).setVisibility(View.GONE);
                    findViewById(R.id.linearLayout2).setVisibility(View.GONE);
                    findViewById(R.id.descriptionCityImage).setVisibility(View.GONE);
                    findViewById(R.id.descriptionCityTitle).setVisibility(View.GONE);
                }

                if (!(currentFragment instanceof FragmentItinerary)) {
                    findViewById(R.id.linearLayoutDays).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.linearLayoutDays).setVisibility(View.VISIBLE);
                    String currentDay = ((FragmentItinerary) currentFragment).dayNumber;
                    setDaysColor(previousDay, currentDay);
                }

                if (currentFragment instanceof FragmentCityConfigureItinerary) {
                    findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
                }

            }
        } else {
            super.onBackPressed();
        }


    }

    private void setDaysColor(String previousDay, String currentDay) {
        if (previousDay != null) {
            switch (previousDay) {
                case "day1":
                    setUnselectedDayColor(R.id.day1);
                    break;
                case "day2":
                    setUnselectedDayColor(R.id.day2);
                    break;
                case "day3":
                    setUnselectedDayColor(R.id.day3);
                    break;
                case "day4":
                    setUnselectedDayColor(R.id.day4);
                    break;
                case "day5":
                    setUnselectedDayColor(R.id.day5);
                    break;
                case "day6":
                    setUnselectedDayColor(R.id.day6);
                    break;
                case "day7":
                    setUnselectedDayColor(R.id.day7);
                    break;
            }
        }
        switch (currentDay) {
            case "day1":
                setSelectedDayColor(R.id.day1);
                break;
            case "day2":
                setSelectedDayColor(R.id.day2);
                break;
            case "day3":
                setSelectedDayColor(R.id.day3);
                break;
            case "day4":
                setSelectedDayColor(R.id.day4);
                break;
            case "day5":
                setSelectedDayColor(R.id.day5);
                break;
            case "day6":
                setSelectedDayColor(R.id.day6);
                break;
            case "day7":
                setSelectedDayColor(R.id.day7);
                break;
        }
    }

    private void setUnselectedDayColor(int day) {
        ((TextView) findViewById(day)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
        ((TextView) findViewById(day)).setTextColor(getResources().getColor(R.color.white));
    }

    private void setSelectedDayColor(int day) {
        ((TextView) findViewById(day)).setBackground(getResources().getDrawable(R.drawable.day_shape));
        ((TextView) findViewById(day)).setTextColor(getResources().getColor(R.color.loginDarkBlue));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        switch (id) {
            case R.id.pick_city:
                toolbar.setTitle(getResources().getString(R.string.pick_a_city));
                replaceFragment(new FragmentPickCity());
                break;
            case R.id.experiences:
                toolbar.setTitle(getResources().getString(R.string.experiences));
                replaceFragment(new FragmentActivities());
                break;
            case R.id.update_profile:
                toolbar.setTitle(getResources().getString(R.string.my_profile));
                replaceFragment(FragmentUpdateProfile.newInstance(LoginActivity.connectedAccount));
                break;
            case R.id.change_password:
                toolbar.setTitle(getResources().getString(R.string.change_password));
                replaceFragment(FragmentChangePassword.newInstance());
                break;
            case R.id.logout:
                LoginActivity.connectedAccount = null;
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            default:
                return true;

        }
        return true;
    }

    public void replaceFragment(Fragment fragment) {

        if (fragment instanceof FragmentCityDescription || fragment instanceof FragmentCityTransport) {
            findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.linearLayout2).setVisibility(View.VISIBLE);
            findViewById(R.id.descriptionCityImage).setVisibility(View.VISIBLE);
            findViewById(R.id.descriptionCityTitle).setVisibility(View.VISIBLE);
        } else if (fragment instanceof FragmentCityAttractions || fragment instanceof FragmentCityAttraction || fragment instanceof FragmentCityConfigureItinerary || fragment instanceof FragmentItinerary) {
            findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.linearLayout2).setVisibility(View.VISIBLE);
            findViewById(R.id.descriptionCityImage).setVisibility(View.GONE);
            findViewById(R.id.descriptionCityTitle).setVisibility(View.GONE);
        } else {
            findViewById(R.id.linearLayout).setVisibility(View.GONE);
            findViewById(R.id.linearLayout2).setVisibility(View.GONE);
            findViewById(R.id.descriptionCityImage).setVisibility(View.GONE);
            findViewById(R.id.descriptionCityTitle).setVisibility(View.GONE);
        }

        if (!(fragment instanceof FragmentItinerary)) {
            findViewById(R.id.linearLayoutDays).setVisibility(View.GONE);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.frameLayout, fragment);
        if (!getSupportFragmentManager().getFragments().isEmpty()) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Nullable
    @Override
    public Display getDisplay() {
        return display;
    }

    public void updateProfileInfo(View view) {
        if (checkUpdateProfileValidation()) {

            class UpdateProfile extends AsyncTask<String, String, String> {

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    Log.i("ups", s);
                }

                @Override
                protected String doInBackground(String... strings) {

                    String server = LoginActivity.server.concat("updateAccount.php");
                    StringBuilder result = new StringBuilder();
                    try {
                        URL url = new URL(server);
                        HttpURLConnection http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("POST");
                        http.setDoInput(true);
                        http.setDoOutput(true);

                        OutputStream output = http.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                        Uri.Builder builder = new Uri.Builder().appendQueryParameter("email", strings[0]).appendQueryParameter("firstName", strings[1]).appendQueryParameter("lastName", strings[2]).appendQueryParameter("country", strings[3]).appendQueryParameter("city", strings[4]).appendQueryParameter("birthDate", strings[5]);
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

            UpdateProfile updateProfile = new UpdateProfile();
            updateProfile.execute(LoginActivity.connectedAccount, firstName.getText().toString(), lastName.getText().toString(), country.getText().toString(), city.getText().toString(), birthDate.getText().toString());
        }
    }

    public boolean checkUpdateProfileValidation() {
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        country = findViewById(R.id.country);
        city = findViewById(R.id.city);
        birthDate = findViewById(R.id.birth_date);

        if (firstName.getText().toString().trim().length() <= 0) {
            firstName.requestFocus();
            firstName.setError("Enter first name");
            return false;
        }
        if (lastName.getText().toString().trim().length() <= 0) {
            lastName.requestFocus();
            lastName.setError("Enter last name");
            return false;
        }
        return true;
    }

    public boolean checkChangePasswordValidation() {
        password = findViewById(R.id.password);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);

        if (password.getText().toString().trim().length() <= 0) {
            password.requestFocus();
            password.setError("Enter current password");
            return false;
        }
        if (newPassword.getText().toString().trim().length() <= 0) {
            newPassword.requestFocus();
            newPassword.setError("Enter the new password");
            return false;
        }
        if (confirmPassword.getText().toString().trim().length() <= 0) {
            confirmPassword.requestFocus();
            confirmPassword.setError("Reenter the new password");
            return false;
        }
        if (!newPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
            confirmPassword.requestFocus();
            confirmPassword.setError("Enter the same new password");
            return false;
        }
        return true;

    }

    public void checkCurrentPassword(View view) {

        if (checkChangePasswordValidation()) {
            class CheckPassword extends AsyncTask<String, String, String> {

                public CheckPassword() {

                }

                @Override
                protected void onPostExecute(String s) {

                    if (s.equals("Login successfully")) {
                        changePassword();
                    } else {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
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
            CheckPassword checkPassword = new CheckPassword();
            checkPassword.execute(LoginActivity.connectedAccount, password.getText().toString());

        }
    }

    private void changePassword() {

        class CheckPassword extends AsyncTask<String, String, String> {

            public CheckPassword() {

            }

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
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
        CheckPassword checkPassword = new CheckPassword();
        checkPassword.execute(LoginActivity.connectedAccount, newPassword.getText().toString());

    }

    public void configureItinerary(View view) {

        if (verifyTheItineraryConfiguration()) {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            accommodationAddress = ((EditText) findViewById(R.id.addressEditText)).getText().toString();

            class Background extends AsyncTask<Void, Void, Void> {

                ArrayList<String> order;
                ArrayList<String> transport;

                @Override
                protected void onPostExecute(Void unused) {
                    replaceFragment(FragmentItinerary.newInstance(order, FragmentCityConfigureItinerary.attractions, transport, "day1"));
                    ((TextView) findViewById(R.id.day1)).setBackground(getResources().getDrawable(R.drawable.day_shape));
                    ((TextView) findViewById(R.id.day1)).setTextColor(getResources().getColor(R.color.loginDarkBlue));
                    ((TextView) findViewById(R.id.day2)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
                    ((TextView) findViewById(R.id.day2)).setTextColor(getResources().getColor(R.color.white));
                    ((TextView) findViewById(R.id.day3)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
                    ((TextView) findViewById(R.id.day3)).setTextColor(getResources().getColor(R.color.white));
                    ((TextView) findViewById(R.id.day4)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
                    ((TextView) findViewById(R.id.day4)).setTextColor(getResources().getColor(R.color.white));
                    ((TextView) findViewById(R.id.day5)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
                    ((TextView) findViewById(R.id.day5)).setTextColor(getResources().getColor(R.color.white));
                    ((TextView) findViewById(R.id.day6)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
                    ((TextView) findViewById(R.id.day6)).setTextColor(getResources().getColor(R.color.white));
                    ((TextView) findViewById(R.id.day7)).setBackground(getResources().getDrawable(R.drawable.round_border_menu_item));
                    ((TextView) findViewById(R.id.day7)).setTextColor(getResources().getColor(R.color.white));
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    ConfigureItinerary configureItinerary = new ConfigureItinerary();
                    order = configureItinerary.getOrder(accommodationAddress);
                    transport = getTransportSelected();
                    return null;
                }
            }

            Background background = new Background();
            background.execute();
        } else {
            Log.i("status", "not ok");
        }


    }

    private ArrayList<String> getTransportSelected() {
        CheckBox car = findViewById(R.id.carCheckBox);
        CheckBox bus = findViewById(R.id.busCheckBox);
        CheckBox subway = findViewById(R.id.subwayCheckBox);
        CheckBox tram = findViewById(R.id.tramCheckBox);
        CheckBox train = findViewById(R.id.trainCheckBox);
        ArrayList<String> transport = new ArrayList<>();
        if (car.isChecked()) {
            transport.add("car");
        }
        if (bus.isChecked()) {
            transport.add("bus");
        }
        if (subway.isChecked()) {
            transport.add("subway");
        }
        if (tram.isChecked()) {
            transport.add("tram");
        }
        if (train.isChecked()) {
            transport.add("train");
        }
        return transport;
    }

    private boolean verifyTheItineraryConfiguration() {

        pickedAttractions = new ArrayList<>();

        RadioGroup group = findViewById(R.id.radioGroup);
        if (group.getCheckedRadioButtonId() == -1) {
            ((TextView) findViewById(R.id.textViewNumberOfDays)).requestFocus();
            ((TextView) findViewById(R.id.textViewNumberOfDays)).setError("Select the number of days");
            return false;
        } else {

        }

        CheckBox car = findViewById(R.id.carCheckBox);
        CheckBox bus = findViewById(R.id.busCheckBox);
        CheckBox subway = findViewById(R.id.subwayCheckBox);
        CheckBox tram = findViewById(R.id.tramCheckBox);
        CheckBox train = findViewById(R.id.trainCheckBox);

        if (!car.isChecked() && !bus.isChecked() && !subway.isChecked() && !tram.isChecked() && !train.isChecked()) {
            ((TextView) findViewById(R.id.textViewTrnsportTypes)).requestFocus();
            ((TextView) findViewById(R.id.textViewTrnsportTypes)).setError("Select at least one transport type");
            return false;
        }

        RecyclerView recyclerView = findViewById(R.id.chooseAttractionsRecyclerView);
        for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
            View view = recyclerView.findViewHolderForAdapterPosition(i).itemView;
            if (((CheckBox) view.findViewById(R.id.pickAttractionCheckBox)).isChecked()) {
                pickedAttractions.add(((TextView) view.findViewById(R.id.attractionName)).getText().toString());
            }
        }
        if (pickedAttractions.size() == 0) {
            ((TextView) findViewById(R.id.textViewAttractions)).requestFocus();
            ((TextView) findViewById(R.id.textViewAttractions)).setError("Select at least one attraction");
            return false;
        } else {
            int id = group.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(id);
            numberOfDays = Integer.valueOf(radioButton.getText().toString());
            Log.i("number:", Integer.toString(numberOfDays));
            if (pickedAttractions.size() > (numberOfDays * 4)) {
                ((TextView) findViewById(R.id.textViewAttractions)).requestFocus();
                if (numberOfDays == 1) {
                    ((TextView) findViewById(R.id.textViewAttractions)).setError("There are too many attractions for " + numberOfDays + " day. Select maximim " + numberOfDays * 4 + ".");
                } else {
                    ((TextView) findViewById(R.id.textViewAttractions)).setError("There are too many attractions for " + numberOfDays + " days. Select maximim " + numberOfDays * 4 + ".");
                }
                return false;
            }
        }

        if (((EditText) findViewById(R.id.addressEditText)).getText().toString().isEmpty()) {
            ((TextView) findViewById(R.id.addressEditText)).requestFocus();
            ((TextView) findViewById(R.id.addressEditText)).setError("Enter the address of your accommodation for the best configuration");
            return false;
        } else {
            Log.i("city", ((TextView) findViewById(R.id.descriptionCityTitle)).getText().toString());
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + ((TextView) findViewById(R.id.descriptionCityTitle)).getText().toString() + "&key=" + ConfigureItinerary.API_KEY;
            Request request = new Request.Builder()
                    .url(url)
                    .method("GET", null)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                JSONObject object = new JSONObject(response.body().string());
                if (!checkBoundaries(((EditText) findViewById(R.id.addressEditText)).getText().toString(), object)) {
                    ((TextView) findViewById(R.id.addressEditText)).requestFocus();
                    ((TextView) findViewById(R.id.addressEditText)).setError("The address is not in the city");
                    return false;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }
        return true;

    }

    private boolean checkBoundaries(String address, JSONObject object) {
        double latNE, lngNE, latSW, lngSW;
        JSONArray results = object.optJSONArray("results");
        if (results != null) {
            JSONObject geometry = results.optJSONObject(0).optJSONObject("geometry");
            if (geometry != null) {
                JSONObject bounds = geometry.optJSONObject("bounds");
                if (bounds != null) {
                    JSONObject ne = bounds.optJSONObject("northeast");
                    latNE = ne.optDouble("lat");
                    lngNE = ne.optDouble("lng");
                    JSONObject sw = bounds.optJSONObject("southwest");
                    latSW = sw.optDouble("lat");
                    lngSW = sw.optDouble("lng");

                    OkHttpClient client = new OkHttpClient().newBuilder().build();
                    String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + ConfigureItinerary.API_KEY;
                    Request request = new Request.Builder()
                            .url(url)
                            .method("GET", null)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        JSONObject accommodationObj = new JSONObject(response.body().string());
                        Log.i("locationResult", accommodationObj.toString());
                        JSONArray accommodationResults = accommodationObj.optJSONArray("results");
                        if (accommodationResults != null && accommodationResults.optJSONObject(0) != null) {
                            JSONObject accommodationGeometry = accommodationResults.optJSONObject(0).optJSONObject("geometry");
                            if (accommodationGeometry != null) {
                                JSONObject location = accommodationGeometry.optJSONObject("location");
                                if (location != null) {
                                    lat = location.optDouble("lat");
                                    lng = location.optDouble("lng");
                                    if (Double.compare(lat, latSW) > 0 && Double.compare(lat, latNE) < 0 && Double.compare(lng, lngSW) > 0 && Double.compare(lng, lngNE) < 0) {
                                        return true;
                                    }
                                }
                            }
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public void setNumberOfDaysNoError(View view) {
        ((TextView) findViewById(R.id.textViewNumberOfDays)).setError(null);
    }

    public void setTransportTypeNoError(View view) {
        ((TextView) findViewById(R.id.textViewTrnsportTypes)).setError(null);
    }

    public void setPickAttractionsNoError(View view) {
        ((TextView) findViewById(R.id.textViewAttractions)).setError(null);
    }

    public void saveItinerary(View view) {
        setCityId();
    }

    public void setCityId() {

        class Background extends AsyncTask<String, String, String> {
            Context context;

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s.equals("The city was not found") || s.equals("Connection error. Try again.")) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                } else {
                    insertItinerary(LoginActivity.idAccount, cityId, MainActivity.numberOfDays, MainActivity.accommodationAddress);
                }
            }

            public Background(Context context) {
                this.context = context;
            }

            @Override
            protected String doInBackground(String... voids) {

                StringBuilder result = new StringBuilder();
                String server = LoginActivity.server.concat("getCityId.php");
                try {
                    URL url = new URL(server);
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");
                    http.setDoInput(true);
                    http.setDoOutput(true);

                    OutputStream output = http.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                    Uri.Builder builder = new Uri.Builder().appendQueryParameter("city", voids[0]);

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
                        cityId = Integer.valueOf(line);
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
        Background background = new Background(MainActivity.this);
        background.execute(FragmentItinerary.city);

    }

    public void insertItinerary(int idAccount, int idCity, int numberOfDays, String accommodationAddress) {

        String idAccountString = Integer.toString(idAccount);
        String idCityString = Integer.toString(idCity);
        String numberOfDaysString = Integer.toString(numberOfDays);

        class Background extends AsyncTask<String, String, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (!s.contains("Itinerary added")) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                } else {
                    ArrayList<String> transport = FragmentItinerary.transport;
                    for (int i = 0; i < transport.size(); i++) {
                        insertTransportItinerary(itineraryId, transport.get(i));
                    }

                    ArrayList<String> attractionsOrder = FragmentItinerary.attractionsOrder;
                    for (int i = 1; i <= numberOfDays; i++) {
                        String day = "day" + i;
                        int index = attractionsOrder.indexOf(day);
                        index++;
                        int nrOfAttractionInADay = 0;
                        while (index < attractionsOrder.size() && !attractionsOrder.get(index).equals("day1") && !attractionsOrder.get(index).equals("day2") && !attractionsOrder.get(index).equals("day3") && !attractionsOrder.get(index).equals("day4") && !attractionsOrder.get(index).equals("day5") && !attractionsOrder.get(index).equals("day6") && !attractionsOrder.get(index).equals("day7")) {
                            if (!attractionsOrder.get(index).equals("accommodation")) {
                                nrOfAttractionInADay++;
                                insertAttraction(itineraryId, attractionsOrder.get(index), day, nrOfAttractionInADay, cityId);
                            }
                            index++;
                        }
                    }

                }
            }

            @Override
            protected String doInBackground(String... strings) {

                String server = LoginActivity.server.concat("insertItinerary.php");
                StringBuilder result = new StringBuilder();
                try {
                    URL url = new URL(server);
                    HttpURLConnection http = (HttpURLConnection) url.openConnection();
                    http.setRequestMethod("POST");
                    http.setDoInput(true);
                    http.setDoOutput(true);

                    OutputStream output = http.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                    Uri.Builder builder = new Uri.Builder().appendQueryParameter("idAccount", strings[0]).appendQueryParameter("idCity", strings[1]).appendQueryParameter("nrOfDays", strings[2]).appendQueryParameter("accommodationAddress", strings[3]);
                    String data = builder.build().getEncodedQuery();
                    writer.write(data);
                    writer.flush();
                    writer.close();
                    output.close();

                    InputStream input = http.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.ISO_8859_1));
                    String line;
                    int nr = 0;
                    while ((line = reader.readLine()) != null) {
                        if (nr == 1) {
                            itineraryId = Integer.valueOf(line);
                        }
                        nr++;
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

        Background background = new Background();
        background.execute(idAccountString, idCityString, numberOfDaysString, accommodationAddress);


    }


    private void insertAttraction(int itineraryId, String attraction, String day, int nrOfAttractionInADay, int cityId) {

        class Background2 extends AsyncTask<String, String, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (!s.isEmpty()) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                {

                    StringBuilder result = new StringBuilder();
                    String server = LoginActivity.server.concat("insertAttractionItinerary.php");
                    try {
                        URL url = new URL(server);
                        HttpURLConnection http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("POST");
                        http.setDoInput(true);
                        http.setDoOutput(true);

                        OutputStream output = http.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                        Uri.Builder builder = new Uri.Builder().appendQueryParameter("idItinerary", strings[0]).appendQueryParameter("attraction", strings[1]).appendQueryParameter("day", strings[2]).appendQueryParameter("nrOfAttraction", strings[3]).appendQueryParameter("cityId", strings[4]);

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
        }
        Background2 background2 = new Background2();
        background2.execute(Integer.toString(itineraryId), attraction, day, Integer.toString(nrOfAttractionInADay), Integer.toString(cityId));

    }

    public void insertTransportItinerary(int itineraryId, String transport) {
        class Background2 extends AsyncTask<String, String, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (!s.isEmpty()) {
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                {

                    StringBuilder result = new StringBuilder();
                    String server = LoginActivity.server.concat("insertTransportItinerary.php");
                    try {
                        URL url = new URL(server);
                        HttpURLConnection http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("POST");
                        http.setDoInput(true);
                        http.setDoOutput(true);

                        OutputStream output = http.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

                        Uri.Builder builder = new Uri.Builder().appendQueryParameter("idItinerary", strings[0]).appendQueryParameter("transportType", strings[1]);

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
        }
        Background2 background2 = new Background2();
        background2.execute(Integer.toString(itineraryId), transport);
    }

}