package com.example.sai_h.reviewuoso;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener {
    double lat, lon;
    Location loc;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mRequest;
    EditText e;
    HttpsGetter h = new HttpsGetter();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        buildGoogleapiclient();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
            String s = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+String.valueOf(lat)+","+String.valueOf(lon)+"&radius=5000&type=restaurant&key=AIzaSyBx77XwaQLYdMOqqlb3n3x6-talZWLhyjk";
            String res = String.valueOf(new HttpsGetter().execute(s));
            result(res);
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
            String s = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+String.valueOf(lat)+","+String.valueOf(lon)+"&radius=5000&type=restaurant&key=AIzaSyBx77XwaQLYdMOqqlb3n3x6-talZWLhyjk";
            String res = String.valueOf(new HttpsGetter().execute(s));
            result(res);
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
        TextView t = (TextView)findViewById(R.id.result);
        t.setText(s);
    }
}




