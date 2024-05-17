package com.example.lostfoundapp;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONObject;

public class AddItem extends AppCompatActivity {
    private static final String TAG = "AddItem";
    private EditText textName, textPhone, textDescription, textDate, textLocation;
    private RadioGroup radioGroup;
    private RadioButton radioLost, radioFound;
    private Button btnSave, btnCancel, btnGetCurrentLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        Places.initialize(getApplicationContext(), "AIzaSyDYa7z5CNYi0Xa5UTTGOLNCP5-d-_nWSgU");
        placesClient = Places.createClient(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        textName = findViewById(R.id.textName);
        textPhone = findViewById(R.id.textPhone);
        textDescription = findViewById(R.id.textDescription);
        textDate = findViewById(R.id.textDate);
        textLocation = findViewById(R.id.textLocation);
        radioGroup = findViewById(R.id.radioGroup);
        radioLost = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnGetCurrentLocation = findViewById(R.id.btnGetCurrentLocation);

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    textLocation.setText(place.getName());
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                }

                @Override
                public void onError(@NonNull com.google.android.gms.common.api.Status status) {
                    Log.i(TAG, "An error occurred: " + status);
                }
            });
        }
    }

    private void setupListeners() {
        textDate.setOnClickListener(v -> showDatePickerDialog());
        btnSave.setOnClickListener(v -> saveItem());
        btnCancel.setOnClickListener(v -> finish());
        btnGetCurrentLocation.setOnClickListener(v -> getCurrentLocation());
    }

    private void getCurrentLocation() {
        Log.d(TAG, "Get Current Location button clicked");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Requesting location permissions");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        try {
            Log.d(TAG, "Fetching last known location");
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d(TAG, "Location obtained: " + latLng);
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    reverseGeocode(latLng);
                } else {
                    Log.d(TAG, "Location is null, requesting new location update");
                    requestNewLocationData();
                }
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Failed to get location", e);
                Toast.makeText(this, "Failed to get current location", Toast.LENGTH_SHORT).show();
            });
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission not granted", e);
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestNewLocationData() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setNumUpdates(1);

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        } catch (SecurityException e) {
            Log.e(TAG, "Location permission not granted", e);
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult == null) {
                Log.d(TAG, "New location update is null");
                return;
            }
            Location location = locationResult.getLastLocation();
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d(TAG, "New location obtained: " + latLng);
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                reverseGeocode(latLng);
            }
        }
    };

    private void reverseGeocode(LatLng latLng) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.latitude + "," + latLng.longitude + "&key=AIzaSyDYa7z5CNYi0Xa5UTTGOLNCP5-d-_nWSgU";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(AddItem.this, "Failed to get address", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            JSONObject json = new JSONObject(responseData);
                            JSONArray results = json.getJSONArray("results");
                            if (results.length() > 0) {
                                JSONObject firstResult = results.getJSONObject(0);
                                String address = firstResult.getString("formatted_address");
                                textLocation.setText(address);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(AddItem.this, "Failed to get address", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String dateString = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    textDate.setText(dateString);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void saveItem() {
        String name = textName.getText().toString();
        String phone = textPhone.getText().toString();
        String description = textDescription.getText().toString();
        String date = textDate.getText().toString();
        String location = textLocation.getText().toString();
        boolean isLost = radioLost.isChecked();

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            Item newItem = new Item(name, phone, description, date, location, isLost, latitude, longitude);
            long result = dbHelper.addItem(newItem);

            if (result != -1) {
                Toast.makeText(this, "Item saved successfully!", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            } else {
                Toast.makeText(this, "Failed to save item", Toast.LENGTH_SHORT).show();
            }
        }
    }
}