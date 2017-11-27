package com.sitecdesarro.gymapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sitecdesarro.gymapp.R;
import com.stephentuso.welcome.WelcomeHelper;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.
        OnNavigationItemSelectedListener  {


    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private WelcomeHelper appInfo;


    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appInfo = new WelcomeHelper(this, AppInfo.class);
        appInfo.show(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //con el metodo forceshow obligamos al usuario a ver esta pantalla
        // cada vez que inicia la aolicacion
//        appInfo.forceShow();


        setupToolbarMenu();
        setupNavigationDrawerMenu();


        // Add the fragment to its container using a FragmentManager and a Transaction
        fragmentManager = getSupportFragmentManager();

        LoginFragment loginFragment = new LoginFragment();
        fragmentManager.beginTransaction()
                .add(R.id.aux_container, loginFragment)
                .commit();


    }






    private void setupToolbarMenu() {

        mToolbar = (Toolbar) findViewById(R.id.toolbarMain);
        // mToolbar.setTitle("Navigation View");


        setSupportActionBar(mToolbar);
    }

    private void setupNavigationDrawerMenu() {
        NavigationView navigationView = (NavigationView) findViewById(R.id
                .navigationView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle drawerToggle = new
                ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string
                .drawer_open, R.string.drawer_close);


        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        appInfo.onSaveInstanceState(outState);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        closeDrawer();


        switch (item.getItemId()) {

            case R.id.itemAbout:

                startActivity(new Intent(MainActivity.this, AboutActivity
                        .class));

                break;
            case R.id.itemContact:

                startActivity(new Intent(MainActivity.this, ContactActivity
                        .class));
                break;

            case R.id.itemReservation:


                startActivity(new Intent(MainActivity.this, ReservationActivity
                        .class));

                break;


            case R.id.itemLogOut:

                startActivity(new Intent(MainActivity.this, LogOutActivity
                        .class));

                break;

            case R.id.itemProfile:

                startActivity(new Intent(MainActivity.this, ProfileActivity
                        .class));
                break;


        }


        return false;
    }

    private void closeDrawer() {

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void openDrawe() {

        mDrawerLayout.openDrawer(GravityCompat.START);

    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

            closeDrawer();
        } else {

            super.onBackPressed();
        }


    }
}
