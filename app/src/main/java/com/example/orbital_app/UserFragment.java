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

        findPreference("profile").setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(getActivity(), Profile.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            return true;
        });

        findPreference("favourites").setOnPreferenceClickListener(preference -> {
            startActivity(new Intent(getActivity(), FavList.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            return true;
        });

        findPreference("preferences").setOnPreferenceClickListener(p -> {
            startActivity(new Intent(getActivity(), PrefForm.class));
            return true;
        });
    }

}
