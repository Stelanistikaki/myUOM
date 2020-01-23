package com.example.admin.myuom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.admin.myuom.Compus.CompusFragment;
import com.example.admin.myuom.Grades.GradesFragment;
import com.example.admin.myuom.News.NewsFragment;
import com.example.admin.myuom.Program.ProgramFragment;
import com.example.admin.myuom.Settings.SettingsFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView toolbarΤitle;
    private CharSequence mTitle;
    private TextView navbarTitle ;
    SharedPreferences sp;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ProgramFragment()).commit();
        }
        sp = getSharedPreferences("pref",MODE_PRIVATE);

        //code for the blue color on the status bar
        //not working with older os

//        Window window = this.getWindow();
//
//        // clear FLAG_TRANSLUCENT_STATUS flag:
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//        // finally change the color
//        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));

        //set the navigation drawer
        toolbarΤitle = (TextView) findViewById(R.id.titleToolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbarΤitle.setText("Πρόγραμμα");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // set a custom shadow that overlays the main content when the drawer opens
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_uomNews, R.id.nav_compus, R.id.nav_grades,
                R.id.nav_settings, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        navigationView.setNavigationItemSelectedListener(this);

    }

    //on back button pressed closes the drawer
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.content_frame);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //when an item in drawer is selected a new fragment loads
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //creating fragment object
        Fragment fragment = null;
        //initializing the fragment object which is selected
        switch (menuItem.getItemId()) {
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                break;
            case R.id.nav_grades:
                fragment = new GradesFragment();
                break;
            case R.id.nav_uomNews:
                fragment = new NewsFragment();
                break;
            case R.id.nav_compus:
                fragment = new CompusFragment();
                break;
            case R.id.nav_program:
                fragment = new ProgramFragment();
                break;
            case R.id.nav_logout:
                //if the user logges out the shared value has to change
                sp.edit().putBoolean("logged",false).apply();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;

        }

        //replacing the fragment
        if (fragment != null ) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        //set the toolbar title according to selected item
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        toolbarΤitle.setText(menuItem.getTitle());
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        toolbarΤitle.setText(mTitle);
    }
}
