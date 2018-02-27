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
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener {
    double lat,lon;
    Location loc;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mRequest;
    EditText e;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        buildGoogleapiclient();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    synchronized void buildGoogleapiclient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onLocationChanged(Location location) {
        try{
            if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mRequest,this);
            loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        catch (Exception e){
            Log.i("Error",e.toString());
        }
        if(loc!=null){
            lat = loc.getLatitude();
            lon = loc.getLongitude();
            updateUI();
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mRequest = LocationRequest.create();
        mRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mRequest.setMaxWaitTime(5000);
        mRequest.setInterval(10000);
        try{
            if(ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mRequest, (com.google.android.gms.location.LocationListener) this);
            loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
        catch (Exception e){
            Log.i("Error",e.toString());
        }
        if(loc!=null){
            lat = loc.getLatitude();
            lon = loc.getLongitude();
            updateUI();
            new HttpURLgetter();
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
    public void updateUI(){
        TextView t = (TextView)findViewById(R.id.lat);
        t.setText("Latitude is"+String.valueOf(lat)+"\n"+"Longitude is"+String.valueOf(lon));
    }
    public void urlOutput()
    {
        try {   Toast.makeText(MainActivity.this,"Entered",Toast.LENGTH_SHORT).show();
                e = (EditText)findViewById(R.id.cat);
                String s = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+String.valueOf(lat)+","+String.valueOf(lon)+"&radius=5000&type=restaurant&key=AIzaSyBx77XwaQLYdMOqqlb3n3x6-talZWLhyjk";
                URL url = new URL(s);
                HttpsURLConnection conn= (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream in = new BufferedInputStream(conn.getInputStream());
                final String html = convertToString(in);
                Runnable r=new Runnable() {
                    @Override
                    public void run() {

                        TextView t1 = (TextView) findViewById(R.id.result);
                        t1.setText(html);
                        System.out.println(html);
                    }
                };
                runOnUiThread(r);


            }
            catch (Exception e) {
                e.printStackTrace();
        }
    }
    private class HttpURLgetter extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            Toast.makeText(MainActivity.this,"Entered",Toast.LENGTH_SHORT).show();
            urlOutput();

            /*
            */return null;
        }
    }
    public String convertToString(InputStream in) throws IOException {
        BufferedReader b = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line=b.readLine())!=null){
            sb.append(line+"\n");
        }
        b.close();
        return sb.toString();
    }
}

