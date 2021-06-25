package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity {

    private RatingBar rating;
    private ImageButton favBtn;
    private Button linkBtn;
    private Button gpsBtn;
    private int totalRating = 0;
    private int numPpl = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef, favRef, favListRef;
    private Boolean favChecker = false;

    private Locations location;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = user.getUid();

    private ArrayList<Reviews> list = new ArrayList<>();

    private FirebaseAnalytics mFirebaseAnalytics;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        getSupportActionBar().setTitle("HangOuts");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = getIntent().getExtras();
        location = new Locations(bundle.getString("location"),
                bundle.getString("image"),
                bundle.getInt("rating"),
                bundle.getString("cost"),
                bundle.getString("state"),
                bundle.getInt("id"),
                bundle.getString("generalLoc"),
                bundle.getString("openingHours"),
                bundle.getString("briefDsc"),
                bundle.getString("link"),
                bundle.getString("postal"),
                bundle.getStringArrayList("activities"));

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

        TextView tv6 = findViewById(R.id.possibleActivities);

        if(location.getActivities().size() == 1){
            tv6.setText("Type of activities:  " + location.getActivities().get(0));
        }else{
            String toBeDisplayed = "";
            for(int i=0; i<location.getActivities().size(); i++){
                if(i<location.getActivities().size()-1){
                    toBeDisplayed +=  location.getActivities().get(i) + ", ";
                }else{
                    toBeDisplayed +=  location.getActivities().get(i);
                }
            }
            tv6.setText("Type of activities:  " + toBeDisplayed);
        }



        TextView tv7 = findViewById(R.id.txtdsc);
        tv7.setText(location.getBriefDsc());

        ImageView iv = findViewById(R.id.image);
        Glide.with(Activity2.this)
                .asBitmap()
                .load(location.getImage())
                .into(iv);

        rating = findViewById(R.id.ratingBar);
        db.collection("reviews").whereEqualTo("place", location.getName())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            Reviews review = dc.getDocument().toObject(Reviews.class);
                            totalRating += review.getRating();
                            numPpl ++;

                        }
                        if(numPpl <= 4){
                            rating.setRating(location.getRating());
                        }
                        else{
                            rating.setRating(Math.round(totalRating/numPpl));
                        }
                    }
                });


        dbRef = rtdb.getReference("allLoc");
        favRef = rtdb.getReference("fav");
        favListRef = rtdb.getReference("favList").child(currentUserId);

        favBtn = findViewById(R.id.favBtn);
        final String postkey = location.getName();
        favouriteChecker(postkey, favBtn);

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                favChecker = true;

                favRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(favChecker.equals(true)){
                            if(snapshot.child(postkey).hasChild(currentUserId)){
                                favRef.child(postkey).child(currentUserId).removeValue();
                                delete(location.getName());
                                favChecker = false;
                                Toast.makeText(Activity2.this, "Removed from favourites", Toast.LENGTH_SHORT).show();
//                                Bundle bun = new Bundle();
//                                bun.putString(FirebaseAnalytics.Param.ITEM_ID, location.getName());
//                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                            }else{
                                favRef.child(postkey).child(currentUserId).setValue(true);

                                String id = favListRef.push().getKey();
                                favListRef.child(id).setValue(location);
                                favChecker = false;
                                Toast.makeText(Activity2.this, "Added to favourites", Toast.LENGTH_SHORT).show();



                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        linkBtn = findViewById(R.id.linkBtn);
        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location.getLink().equals("")){
                    Toast.makeText(Activity2.this, "No website available", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Activity2.this, "Opening link...", Toast.LENGTH_SHORT).show();
                    gotoUrl(location.getLink());
                }

            }
        });

        gpsBtn = findViewById(R.id.gpsBtn);
        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(Activity2.this, "Opening Google Maps", Toast.LENGTH_SHORT).show();
                Toast.makeText(Activity2.this, "https://www.google.com.sg/maps/dir/" + "%" + "/" + location.getPostal(), Toast.LENGTH_LONG).show();
                gotoUrl("https://www.google.com.sg/maps/dir/" + "%" + "/" + location.getPostal());
            }
        });

        TextView reviewTab = findViewById(R.id.reviewTab);
        reviewTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity2.this, ReviewActivity.class);
                i.putExtra("locName", location.getName());
                startActivity(i);
                overridePendingTransition(0,0);
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation_2);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        break;

                    case R.id.nav_favourites:

                        startActivity(new Intent(getApplicationContext(), FavList.class));
                        overridePendingTransition(0,0);
                        break;

                    case R.id.nav_search:

                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0,0);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

    private void favouriteChecker(String postkey, ImageButton favBtn) {
        favRef = rtdb.getReference("fav");
        favRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postkey).hasChild(currentUserId)){
                    favBtn.setImageResource(R.drawable.ic_red_fave);
                }else{
                    favBtn.setImageResource(R.drawable.ic_fave);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    private void gotoUrl(String s){
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    private void delete(String name){
        Query query = favListRef.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    ds.getRef().removeValue();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}