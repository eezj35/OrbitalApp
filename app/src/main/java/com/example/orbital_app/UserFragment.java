package com.example.orbital_app;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class UserFragment extends PreferenceFragment {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_preference);

        findPreference("favourites").setOnPreferenceClickListener(preference -> {
            //@Justin, do ur startActivity here
//            startActivity(new Intent(getActivity(), About.class));
            Toast.makeText(getContext(), "hi", Toast.LENGTH_SHORT).show();
            return true;
        });
    }

}
