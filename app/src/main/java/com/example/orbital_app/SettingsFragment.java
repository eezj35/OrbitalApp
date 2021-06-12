package com.example.orbital_app;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.SwitchPreference;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

public class SettingsFragment extends PreferenceFragment {

//    Preference gps;
//    FusedLocationProviderClient fusedLocationProviderClient;
//    LocationRequest locationRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_preference);

        findPreference("about").setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(getActivity(), About.class));
            return true;
        });

//        locationRequest = LocationRequest.create();
//        locationRequest.setInterval(30000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//
//        findPreference("gps").setOnPreferenceClickListener(preference -> {
//
//        });
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Executor) this, new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//
//                }
//            });
//        }
    }
}
