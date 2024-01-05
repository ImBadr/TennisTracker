package com.example.tennistracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Locale;

public class AddNewMatchFragment extends Fragment {

    private static final String url = "jdbc:mysql://10.0.2.2:3306/androidApp";
    private static final String user = "root";
    private static final String pass = "password";
    private static final String insertRequest = "INSERT INTO `tennis_match` (player1, player2, player1_score, player2_score, location) VALUES (?,?,?,?,?);";

    double lat, lon;
    TextView locationTextView;
    FusedLocationProviderClient client;

    public AddNewMatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_match, container, false);

        EditText player1 = view.findViewById(R.id.player1);
        EditText player2 = view.findViewById(R.id.player2);
        EditText player1_score = view.findViewById(R.id.player1_score);
        EditText player2_score = view.findViewById(R.id.player2_score);
        locationTextView = view.findViewById(R.id.location);
        Button locations_button = view.findViewById(R.id.logo_location);
        Button submit = view.findViewById(R.id.submit_button);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        locations_button.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        });

        submit.setOnClickListener(v -> {
            if(validateEditTexts(player1, player2, player1_score, player2_score, locationTextView))
            connectionDB(
                player1.getText().toString(),
                player2.getText().toString(),
                player1_score.getText().toString(),
                player2_score.getText().toString(),
                    locationTextView.getText().toString());

            Toast.makeText(getActivity(), "Match added", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getCurrentLocation();
        } else {
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(
                    task -> {
                        Location location = task.getResult();
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();

                            Geocoder geocoder;
                            List<Address> addresses;
                            geocoder = new Geocoder(getActivity(), Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(lat, lon, 1);
                                String address = addresses.get(0).getAddressLine(0);
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();

                                String fullAddress = address + " " + city + " " + state + " " + country + " " + postalCode;
                                locationTextView.setText(fullAddress);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void
                                onLocationResult(LocationResult locationResult) {
                                    Location location1 = locationResult.getLastLocation();
                                    lat = location1.getLatitude();
                                    lon = location1.getLongitude();

                                    Geocoder geocoder;
                                    List<Address> addresses;
                                    geocoder = new Geocoder(getActivity(), Locale.getDefault());

                                    try {
                                        addresses = geocoder.getFromLocation(lat, lon, 1);
                                        String address = addresses.get(0).getAddressLine(0);
                                        String city = addresses.get(0).getLocality();
                                        String state = addresses.get(0).getAdminArea();
                                        String country = addresses.get(0).getCountryName();
                                        String postalCode = addresses.get(0).getPostalCode();

                                        String fullAddress = address + " " + city + " " + state + " " + country + " " + postalCode;

                                        locationTextView.setText(fullAddress);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };

                            client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public void connectionDB(String player1, String player2, String player1_score, String player2_score, String location) {
        new Thread(() -> {
            try {
                Connection connection = DriverManager.getConnection(url, user, pass);

                PreparedStatement pst = connection.prepareStatement(insertRequest);
                pst.setString(1, player1);
                pst.setString(2, player2);
                pst.setString(3, player1_score);
                pst.setString(4, player2_score);
                pst.setString(5, location);
                pst.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private boolean validateEditTexts(EditText player1, EditText player2, EditText player1_score, EditText player2_score, TextView location) {
        boolean valid = true;

        if (player1.getText().toString().trim().equals("")) {
            player1.setError("Please provide the player 1 name");
            valid = false;
        }
        if (player2.getText().toString().trim().equals("")) {
            player2.setError("Please provide the player 2 name");
            valid = false;
        }
        if (player1_score.getText().toString().trim().equals("")) {
            player1_score.setError("Please provide the player 1 score");
            valid = false;
        }
        if (player2_score.getText().toString().trim().equals("")) {
            player2_score.setError("Please provide the player 2 score");
            valid = false;
        }
        if (location.getText().toString().trim().equals("")) {
            location.setError("Please provide and address by clicking the location icon ");
            valid = false;
        }
        return valid;
    }

}