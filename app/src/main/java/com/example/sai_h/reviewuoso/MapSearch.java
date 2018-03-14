package com.example.sai_h.reviewuoso;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by sai_h on 06-03-2018.
 */

public class MapSearch extends Fragment{
    SupportPlaceAutocompleteFragment autocompleteFragment;
    TextView[] t = new TextView[3];
    View f;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mapsearch,container,false);
        t[0] = v.findViewById(R.id.title);
        t[1] = v.findViewById(R.id.addr);
        t[2] = v.findViewById(R.id.rating);
        autocompleteFragment = new SupportPlaceAutocompleteFragment();
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        f = v.findViewById(R.id.placeauto);
        ft.replace(R.id.placeauto,autocompleteFragment);
        ft.commit();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autocompleteFragment.getView().setVisibility(View.GONE);
            }
        });
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                t[0].setText(place.getName());
                t[1].setText(place.getAddress());
                t[2].setText(String.valueOf(place.getRating()));
            }

            @Override
            public void onError(Status status) {

            }
        });
    }
}
