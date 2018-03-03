package com.example.sai_h.reviewuoso;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
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
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener,AdapterView.OnItemSelectedListener{
    double lat, lon;
    Location loc;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mRequest;
    Spinner sp;
    RecyclerView r;
    String[] category = new String[]{"atm","bakery","bank","bar","beauty_salon","book_store","cafe","clothing_store","department_store","electronics_store","gas_station","hospital","library","pharmacy","restaurant","school","shoe_store","shopping_mall","supermarket"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        buildGoogleapiclient();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sp = (Spinner)findViewById(R.id.spin);
        sp.setOnItemSelectedListener(this);
        r = (RecyclerView) findViewById(R.id.recycle);
        r.setLayoutManager(new LinearLayoutManager(this));
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int i1 = sp.getSelectedItemPosition();
        Log.i("Pos",String.valueOf(i));
        String cat = category[i1];
        String s = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+String.valueOf(lat)+","+String.valueOf(lon)+"&radius=5000&type="+cat+"&key=AIzaSyBx77XwaQLYdMOqqlb3n3x6-talZWLhyjk";
        List<Placeresult> places;
        places = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(new HttpsGetter().execute(s).get());
            JSONArray jarr1 = json.getJSONArray("results");
            System.out.println(lat+"\n"+lon);
            System.out.println(jarr1.getJSONObject(0).getString("rating"));
            int it =0;
            for(it=0;it<jarr1.length();it++){
                JSONObject j2 = jarr1.getJSONObject(it);
                if(jarr1.getJSONObject(it).has("rating")&&jarr1.getJSONObject(it).getDouble("rating")>3.0)
                    places.add(new Placeresult("Rating:"+jarr1.getJSONObject(it).getString("rating"),jarr1.getJSONObject(it).getString("name"),jarr1.getJSONObject(it).getString("vicinity")));
            }
            PlaceAdapter p = new PlaceAdapter(this,places);
            r.setAdapter(p);
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}




