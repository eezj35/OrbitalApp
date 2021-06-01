package com.example.orbital_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class Activity2 extends AppCompatActivity {

    private RatingBar rating;
    private Button favBtn;
    private Button linkBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Bundle bundle = getIntent().getExtras();
        Locations location = new Locations(bundle.getString("location"),
                bundle.getString("image"),
                bundle.getInt("rating"),
                bundle.getString("cost"),
                bundle.getString("state"),
                bundle.getInt("id"),
                bundle.getString("generalLoc"),
                bundle.getString("openingHours"),
                bundle.getString("briefDsc"),
                bundle.getString("link"));

        TextView tv1 = findViewById(R.id.txtName);
        tv1.setText(location.getName());
        TextView tv2 = findViewById(R.id.costInfo);
        tv2.setText("Cost:  " + location.getCost());
        TextView tv3 = findViewById(R.id.stateInfo);
        tv3.setText(location.getState());
        TextView tv4 = findViewById(R.id.gLInfo);
        tv4.setText("General Location:  " + location.getGeneralLoc());
        TextView tv5 = findViewById(R.id.oHINfo);
        tv5.setText("Opening Hours:  " + location.getOpeningHours());
        TextView tv6 = findViewById(R.id.txtdsc);
        tv6.setText(location.getBriefDsc());

        ImageView iv = findViewById(R.id.image);

        Glide.with(Activity2.this)
                .asBitmap()
                .load(location.getImage())
                .into(iv);


        rating = findViewById(R.id.ratingBar);
        rating.setRating(location.getRating());

        favBtn = findViewById(R.id.favBtn);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity2.this, "Added to favourites", Toast.LENGTH_SHORT).show();
            }
        });
        linkBtn = findViewById(R.id.linkBtn);
        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity2.this, "Opening link...", Toast.LENGTH_SHORT).show();
                gotoUrl(location.getLink());
            }
        });

    }

    private void gotoUrl(String s){
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}