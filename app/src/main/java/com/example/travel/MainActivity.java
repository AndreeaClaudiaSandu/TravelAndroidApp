package com.example.travel;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Display display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setFrameLayoutHeight();

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
                if(currentFragment instanceof FragmentCityDescription || currentFragment instanceof FragmentCityTransport){
                    if(findViewById(R.id.descriptionCityImage)!=null) {
                        findViewById(R.id.descriptionCityImage).setVisibility(View.VISIBLE);
                        findViewById(R.id.descriptionCityTitle).setVisibility(View.VISIBLE);
                    }
                }
                if (currentFragment instanceof FragmentCityAttractions || currentFragment instanceof  FragmentCityAttraction || currentFragment instanceof  FragmentCityConfigureItinerary){
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
                default:
                    return true;

            }
        return true;
    }

    public void replaceFragment(Fragment fragment) {
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
}