package com.example.sai_h.reviewuoso;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by sai_h on 05-03-2018.
 */

public class MapOpt extends Fragment{
    Spinner sp;
    RecyclerView r;
    String[] category = new String[]{"atm","bakery","bank","bar","beauty_salon","book_store","cafe","clothing_store","department_store","electronics_store","gas_station","hospital","library","pharmacy","restaurant","school","shoe_store","shopping_mall","supermarket"};
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState){
        View rootview = inflater.inflate(R.layout.mapopt,container,false);
        r = (RecyclerView)rootview.findViewById(R.id.recycle);
        sp = (Spinner)rootview.findViewById(R.id.spin);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        r.setLayoutManager(lm);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int i1 = sp.getSelectedItemPosition();
                String cat = category[i1];
                MainActivity m = new MainActivity();
                String s = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+String.valueOf(m.lat)+","+String.valueOf(m.lon)+"&radius=5000&type="+cat+"&key=AIzaSyBx77XwaQLYdMOqqlb3n3x6-talZWLhyjk";
                List<Placeresult> places;
                places = new ArrayList<>();
                try {
                    JSONObject json = new JSONObject(new HttpsGetter().execute(s).get());
                    JSONArray jarr1 = json.getJSONArray("results");
                    System.out.println(m.lat+"\n"+m.lon);
                    System.out.println(jarr1.getJSONObject(0).getString("rating"));
                    int it =0;
                    for(it=0;it<jarr1.length();it++){
                        JSONObject j2 = jarr1.getJSONObject(it);
                        if(jarr1.getJSONObject(it).has("rating")&&jarr1.getJSONObject(it).getDouble("rating")>3.0)
                            places.add(new Placeresult("Rating:"+jarr1.getJSONObject(it).getString("rating"),jarr1.getJSONObject(it).getString("name"),jarr1.getJSONObject(it).getString("vicinity")));
                    }
                    PlaceAdapter p = new PlaceAdapter(getActivity(),places);
                    r.setAdapter(p);
                    r.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        });

    }
}
