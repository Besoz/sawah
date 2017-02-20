package com.accorpa.sawah;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.accorpa.sawah.Handlers.DataHandler;
import com.accorpa.sawah.Handlers.NavigationHandler;
import com.accorpa.sawah.Handlers.SharingHandler;
import com.accorpa.sawah.custom_views.CustomTextView;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int drawerGravity = Gravity.RIGHT;

    private LinearLayout mProgressView;
    private ViewGroup mainLayout;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setLocle();
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        version 23.1.0 switches NavigationView to using a RecyclerView This means it is not
//        instantly available to call findViewById() -a layout pass is needed before it is attached to the
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_base);

        if(DataHandler.getInstance(this).userExist()){

            headerLayout.setVisibility(View.VISIBLE);


            CustomTextView userNameText = (CustomTextView) headerLayout.findViewById(R.id.user_name);
            userNameText.setText(DataHandler.getInstance(this).getUser().getUserName());


            ImageButton settingsButton = (ImageButton) headerLayout.findViewById(R.id.settings_button);
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavigationHandler.getInstance().startEditProfileActivity(BaseActivity.this);
                }
            });



        }

        mainLayout = (ViewGroup) findViewById(R.id.content_base);
        mProgressView = (LinearLayout) findViewById(R.id.base_progress_bar);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(getLayoutResourceId(), null, false);
        mainLayout.addView(contentView);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(drawerGravity)) {
            drawer.closeDrawer(drawerGravity);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(getActionBarMenuLayout(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_change_city) {
            // Handle the camera action

            NavigationHandler.getInstance().startCityActivity(this);

        }
        else if (id == R.id.nav_home) {
            NavigationHandler.getInstance().startCategoriesListActivity(this);
        }
        else if (id == R.id.nav_fav_list) {
            NavigationHandler.getInstance().startFavouritePlacesList(this);
        }else if (id == R.id.nav_about_sawah){
            NavigationHandler.getInstance().startAboutSawah(this);
        } else if (id == R.id.nav_general_inst){
            NavigationHandler.getInstance().startGeneralInstruction(this);
        } else if (id == R.id.nav_contact_us){
            SharingHandler.getInstance().contactSawah(this);
        }
// else if (id == R.id.nav_add_place) {
//
//        } else if (id == R.id.nav_favourites) {
//
//        } else if (id == R.id.nav_contact_us) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(drawerGravity);
        return true;
    }

    protected int getLayoutResourceId() {
        return R.layout.content_base;
    }

    public void setLocle() {
// Log.e("setLocle", lang);

        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    protected int getActionBarMenuLayout() {
        return R.menu.base;
    }


    protected void showProgress(final boolean show) {
            // and hide the relevant UI components.
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mainLayout.setVisibility(show ? View.GONE : View.VISIBLE);

    }
}
