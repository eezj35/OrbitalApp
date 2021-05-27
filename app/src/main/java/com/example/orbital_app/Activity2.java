package com.example.orbital_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Activity2 extends AppCompatActivity {

    private RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Bundle bundle = getIntent().getExtras();
        Locations location = new Locations(bundle.getString("location"), bundle.getString("image"), bundle.getInt("rating"));

        TextView tv = findViewById(R.id.txtName);
        tv.setText(location.getName());

        ImageView iv = findViewById(R.id.image);

        Glide.with(Activity2.this)
                .asBitmap()
                .load(location.getImage())
                .into(iv);


        rating = findViewById(R.id.ratingBar);
        rating.setRating(location.getRating());
    }
}