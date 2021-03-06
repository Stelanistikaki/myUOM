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
import com.example.admin.myuom.Grades.GradesTask;
import com.example.admin.myuom.News.NewsFragment;
import com.example.admin.myuom.Program.ProgramFragment;
import com.example.admin.myuom.Settings.SettingsFragment;
import com.example.admin.myuom.Twitter.TweetFragment;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView toolbarΤitle;
    private CharSequence mTitle;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MenuItem menuItemforRefresh ;
    SharedPreferences sp;
    ArrayList<Lesson> lessons;
    ArrayList<Lesson> unpassedLessons;
    LessonsTask task = new LessonsTask();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.pullToRefreshInternet);
        sp = getSharedPreferences("pref", Context.MODE_PRIVATE);

        //this checks if the app has been opened
        if (savedInstanceState == null) {
            //it has to check if there is internet connection here or else the program fragment will start
            //without Internet connection
            if(!isOnline()){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new NoInternetFragment());
                ft.commit();
            }else{
                //this gets the lessons from the API
                // and gives them to the program and grades fragments
                try {
                    lessons = task.execute().get();
                    GradesTask unpassedTask = new GradesTask(sp.getString("id", ""), lessons, sp.getInt("semester", 0));
                    unpassedLessons = unpassedTask.execute().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ProgramFragment(lessons)).commit();
            }
        }

        //code for the blue color on the status bar
        //not working with older os
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));

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
                R.id.nav_uomNews, R.id.nav_compus, R.id.nav_grades, R.id.nav_email,
                R.id.nav_settings, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        navigationView.setNavigationItemSelectedListener(this);

        //when the user refreshes the screen with swipe down it checks for internet connection
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //if there is not internet connection the no internet fragment is used
                if(!isOnline()) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, new NoInternetFragment());
                    ft.commit();
                //if there is internet it has to reload the same menu item that it was selected (if it was one clicked)
                }else{
                    //if the user had selected an item before
                    if(menuItemforRefresh != null) {
                        onNavigationItemSelected(menuItemforRefresh);
                    }
                    //if there is not an item selected
                    else
                        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ProgramFragment(lessons)).commit();
                }
                //stop refreshing
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    //on back button pressed closes the drawer or the app
    @Override
    public void onBackPressed() {
        //if the menu drawer is open the back button closes it
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        //else if the drawer is not open the app finishes
        else if( menuItemforRefresh == null || menuItemforRefresh.getItemId() == R.id.nav_program ){
            finish();
        }
        //if the user has navigated to another page the back button returns to the program (main screen)
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ProgramFragment(lessons)).commit();
            menuItemforRefresh = null;
            toolbarΤitle.setText("Πρόγραμμα");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.content_frame);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //when an item in drawer is selected a new fragment loads
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent;
        //creating fragment object
        Fragment fragment = null;
        //initializing the fragment object which is selected
        switch (menuItem.getItemId()) {
            case R.id.nav_settings:
                fragment = new SettingsFragment(unpassedLessons);
                break;
            case R.id.nav_grades:
                fragment = new GradesFragment(lessons);
                break;
            case R.id.nav_uomNews:
                fragment = new NewsFragment(swipeRefreshLayout);
                break;
            case R.id.nav_tweet:
                fragment = new TweetFragment();
                break;
            case R.id.nav_compus:
                fragment = new CompusFragment();
                break;
            case R.id.nav_openeclass:
                fragment = new OpenEclassFragment(swipeRefreshLayout);
                break;
            case R.id.nav_program:
                fragment = new ProgramFragment(lessons);
                break;
            case R.id.nav_email:
                intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                //this is used for the open eclass cookies
                //only if the user signs out from the app
                clearCookies();
                sp.edit().putBoolean("logged",false).apply();
                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
        }
        //the email does not have a UI in the app so the previous selected item will remain
        if( menuItem.getItemId() != R.id.nav_logout && menuItem.getItemId() != R.id.nav_email)
            menuItemforRefresh = menuItem;

        //if the user chooses anything but the compus (which is webview with zoom option)
        //he can use the swipe to refresh layout
        swipeRefreshLayout.setEnabled(true);
        if(menuItem.getItemId() == R.id.nav_compus)
            swipeRefreshLayout.setEnabled(false);

        //replacing the fragment
        if (fragment != null && isOnline()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            toolbarΤitle.setText(menuItem.getTitle());
        //this is for the email selection | it will keep showing the program
        }else if(fragment == null){
            //this is for the case that the user has not selected another menu item only the email (first)
            if(menuItemforRefresh != null && menuItemforRefresh.getItemId() != R.id.nav_email){
                onNavigationItemSelected(menuItemforRefresh);
                toolbarΤitle.setText((String) menuItemforRefresh.getTitle());
            }else {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new ProgramFragment(lessons));
                ft.commit();
            }
        }else{
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new NoInternetFragment());
            ft.commit();
            toolbarΤitle.setText(menuItem.getTitle());
        }

        //set the toolbar title according to selected item
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        toolbarΤitle.setText(mTitle);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void clearCookies () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //clean the cookies from open eclass view and log out the user who was before logged in
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        }
    }

}


