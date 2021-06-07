package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
    private Button favBtn;
    private Button linkBtn;
    int totalRating = 0;
    int numPpl = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    DatabaseReference dbRef, favRef, favListRef;
    Boolean favChecker = false;

    Locations location;


    ArrayList<Reviews> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();


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
                        if(numPpl > 1){
                            rating.setRating(Math.round(totalRating/numPpl));
                        }else{
                            rating.setRating(totalRating);
                        }
                    }
                });


        dbRef = rtdb.getReference("allLoc");
        favRef = rtdb.getReference("fav");
        favListRef = rtdb.getReference("favList").child(currentUserId);


        favBtn = findViewById(R.id.favBtn);
        final String postkey = location.getName();

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

        CardView reviewTab = findViewById(R.id.reviewTab);
        reviewTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity2.this, ReviewActivity.class);
                i.putExtra("locName", location.getName());
                startActivity(i);
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

//    private void favChecker(String postkey){
//
//    }
}