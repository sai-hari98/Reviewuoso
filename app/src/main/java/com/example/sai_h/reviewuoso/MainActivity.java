package com.example.sai_h.reviewuoso;
import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener{
    static double lat, lon;
    Location loc;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mRequest;
    ViewPager vp;
    PagerAdapter pa;
    TabLayout tl;
    DrawerLayout d;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar t1 = findViewById(R.id.my);
        setSupportActionBar(t1);
        android.support.v7.app.ActionBar a = getSupportActionBar();
        a.setDisplayHomeAsUpEnabled(true);
        a.setHomeAsUpIndicator(R.drawable.ic_menu);
        d = findViewById(R.id.drawer);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        buildGoogleapiclient();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        vp = (ViewPager)findViewById(R.id.viewpg);
        pa = new PagerAdapter(getSupportFragmentManager(),MainActivity.this);
        vp.setAdapter(pa);
        tl = (TabLayout)findViewById(R.id.tab);
        tl.setupWithViewPager(vp);
        for(int i=0;i<tl.getTabCount();i++){
            TabLayout.Tab t = tl.getTabAt(i);
            t.setCustomView(pa.getTabView(i));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.home: d.openDrawer(GravityCompat.START);
                            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    synchronized void buildGoogleapiclient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mRequest, this);
            loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (Exception e) {
            Log.i("Error", e.toString());
        }
        if (loc != null) {
            lat = loc.getLatitude();
            lon = loc.getLongitude();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mRequest = LocationRequest.create();
        mRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mRequest.setMaxWaitTime(5000);
        mRequest.setInterval(10000);
        try {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mRequest, (com.google.android.gms.location.LocationListener) this);
            loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (Exception e) {
            Log.i("Error", e.toString());
        }
        if (loc != null) {
            lat = loc.getLatitude();
            lon = loc.getLongitude();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleapiclient();
    }
    public void result(String s){

    }
    class PagerAdapter extends FragmentPagerAdapter{
        String tabTitles[] = new String[] {"Nearby Search", "Custom Search"};
        Context con;
        public PagerAdapter(FragmentManager fm,Context context) {
            super(fm);
            this.con = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0: return new MapOpt();
                case 1: return new MapSearch();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
        public View getTabView(int position){
            View tab = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab,null);
            TextView t=(TextView)tab.findViewById(R.id.custom_text);
            t.setText(tabTitles[position]);
            return tab;
        }
    }
}



