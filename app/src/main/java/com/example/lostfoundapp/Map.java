package com.example.lostfoundapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.lostfoundapp.databinding.MapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Item> itemList = dbHelper.getAllItems();

        for (Item item : itemList) {
            LatLng itemLocation = new LatLng(item.getLatitude(), item.getLongitude());
            mMap.addMarker(new MarkerOptions().position(itemLocation).title(item.getName()));
        }

        if (!itemList.isEmpty()) {
            LatLng firstItemLocation = new LatLng(itemList.get(0).getLatitude(), itemList.get(0).getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstItemLocation, 10));
        }
    }
}