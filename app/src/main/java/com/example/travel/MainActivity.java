package com.example.travel;

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
import android.widget.EditText;
import android.widget.FrameLayout;
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

import com.google.android.material.navigation.NavigationView;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Display display;
    EditText firstName, lastName, country, city, birthDate, password, newPassword, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                } else if (currentFragment instanceof FragmentCityAttractions || currentFragment instanceof FragmentCityAttraction || currentFragment instanceof FragmentCityConfigureItinerary) {
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

            }
        } else {
            super.onBackPressed();
        }


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
        } else if (fragment instanceof FragmentCityAttractions || fragment instanceof FragmentCityAttraction || fragment instanceof FragmentCityConfigureItinerary) {
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

}