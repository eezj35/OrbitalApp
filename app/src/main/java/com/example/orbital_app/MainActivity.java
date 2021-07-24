package com.example.orbital_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.remote.WatchChange;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recommendedRecView;
    private RecyclerView topRatedRecView;
    private RecyclerView newRecView;
    private BottomNavigationView bottomNavigationView;

    ArrayList<Locations> recommendedList = new ArrayList<>();
    ArrayList<Locations> topRatedList = new ArrayList<>();
    ArrayList<Locations> newList = new ArrayList<>();

    ProgressDialog pd;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = user.getUid();

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button applyBtn;
    private ImageButton cancelBtn;

    UserInfo userInfo;
    PrefActivities prefActivities;
    String location;
    String prefActivity1;
    String prefActivity2;
    String prefActivity3;

    boolean doubleBackToExitPressedOnce = false;

    private DatabaseReference userPrefRef = rtdb.getReference("pref").child(currentUserId);
    private DatabaseReference userPrefRefActivities = rtdb.getReference("pref").child(currentUserId).child("activities");
    private Boolean isCheck;

    HashMap<String, Boolean> costMap = new HashMap<>();
    HashMap<String, Boolean> stateMap = new HashMap<>();
    HashMap<String, Boolean> activityMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();

        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Loading...");
        pd.show();

        userPrefRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfo = snapshot.getValue(UserInfo.class);
                if(userInfo != null){
                    isCheck = true;
                }

                if (isCheck == null || !isCheck) {
                    startActivity(new Intent(MainActivity.this, WelcomePage.class));
                }

                newRecView = findViewById(R.id.newRV);

                newRecView.setHasFixedSize(true);
                newRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                userInfo = snapshot.getValue(UserInfo.class);

                if(userInfo != null){
                    location = userInfo.getHouse();
                    TextView header = findViewById(R.id.near_header);
                    header.setText("In the "+location);
                    querying(db.collection("places"), "generalLoc", newList, newRecView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        userPrefRefActivities.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recommendedRecView = findViewById(R.id.recommendedRV);
                recommendedRecView.setHasFixedSize(true);
                recommendedRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

                prefActivities = snapshot.getValue(PrefActivities.class);
                if(prefActivities!=null){
                   prefActivity1 = prefActivities.getActivity1();
                   prefActivity2 = prefActivities.getActivity2();
                   prefActivity3 = prefActivities.getActivity3();
                    querying(db.collection("places"), "name", recommendedList, recommendedRecView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        topRatedRecView = findViewById(R.id.topRatedRV);
        topRatedRecView.setHasFixedSize(true);
        topRatedRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        queryingRating(db.collection("places"), topRatedList, topRatedRecView);

        ImageButton recommendedFilter = findViewById(R.id.recommendedFilter);
        recommendedFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog(recommendedRecView, recommendedList, 1);
            }
        });

        ImageButton topRatedFilter = findViewById(R.id.topRatedFilter);
        topRatedFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog(topRatedRecView, topRatedList, 2);
            }
        });

        ImageButton nearestFilter = findViewById(R.id.nearestFilter);
        nearestFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog(newRecView, newList, 1);
            }
        });




        //overlay for bottom navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem mI = menu.getItem(0);
        mI.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;

            }
        }, 2000);

    }

    //overlay for bottom navigation
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.nav_favourites:

                            startActivity(new Intent(getApplicationContext(), FavList.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            break;

                        case R.id.nav_search:

                            startActivity(new Intent(getApplicationContext(), Search.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

                            break;

                        case R.id.nav_user:

                            startActivity(new Intent(getApplicationContext(), User.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

                            break;
                    }
                    return true;
                }
            };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                startActivity(new Intent(this, About.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoggingIn.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void querying(Query query, String order, ArrayList<Locations> list, RecyclerView rv){

        FSDataAdapter adapter = new FSDataAdapter(MainActivity.this, list);
        query.orderBy(order, Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for(DocumentChange dc : value.getDocumentChanges()){
                            Locations loc = dc.getDocument().toObject(Locations.class);
                            if (order.equals("generalLoc")) {

                                if (dc.getType() == DocumentChange.Type.ADDED && location != null) {
                                    if ((loc.getGeneralLoc().equals(location))) {
                                        list.add(loc);
                                    }
                                }

                            } else {
                                if (dc.getType() == DocumentChange.Type.ADDED && prefActivities != null) {
                                    for(String s : loc.getActivities()){
                                        if(s.equals(prefActivity1) || s.equals(prefActivity2) || s.equals(prefActivity3)){
                                                list.add(loc);
                                                break;
                                        }
                                    }
                                }

                            }

                        }
                        adapter.notifyDataSetChanged();
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                });
        rv.setAdapter(adapter);

    }

    private void queryingRating(Query query, ArrayList<Locations> list, RecyclerView rv) {

        DataAdapter2 adapter = new DataAdapter2(MainActivity.this, list);
        query.orderBy("name", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            Locations loc = dc.getDocument().toObject(Locations.class);

                            if (dc.getType() == DocumentChange.Type.ADDED && (loc.getRating() == 5)) {
                                list.add(loc);
                            }

                        }
                        adapter.notifyDataSetChanged();
                        if (pd.isShowing()) {
                            pd.dismiss();
                        }
                    }
                });
        rv.setAdapter(adapter);
    }

    public void filterDialog(RecyclerView rv, ArrayList<Locations> arrayList, int num) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopup = getLayoutInflater().inflate(R.layout.popup, null);

        applyBtn = contactPopup.findViewById(R.id.filterApplyBtn);
        cancelBtn = contactPopup.findViewById(R.id.filterCancelBtn);
        TextView selectAll1 = contactPopup.findViewById(R.id.costSelectAll);
        Button btn1 = contactPopup.findViewById(R.id.filter_btn_low);
        Button btn2 = contactPopup.findViewById(R.id.filter_btn_med);
        Button btn3 = contactPopup.findViewById(R.id.filter_btn_high);
        Button btn9 = contactPopup.findViewById(R.id.filter_btn_free);

        TextView selectAll2 = contactPopup.findViewById(R.id.oiSelectAll);
        Button btn4 = contactPopup.findViewById(R.id.filter_btn_outdoor);
        Button btn5 = contactPopup.findViewById(R.id.filter_btn_indoor);

        TextView selectAll3 = contactPopup.findViewById(R.id.paSelectAll);
        Button btn6 = contactPopup.findViewById(R.id.filter_btn_act1);
        btn6.setText(prefActivity1);
        Button btn7 = contactPopup.findViewById(R.id.filter_btn_act2);
        btn7.setText(prefActivity2);
        Button btn8 = contactPopup.findViewById(R.id.filter_btn_act3);
        btn8.setText(prefActivity3);

        dialogBuilder.setView(contactPopup);
        dialog = dialogBuilder.create();
        dialog.show();

        FSDataAdapter unchangedAdapter = new FSDataAdapter(MainActivity.this, arrayList);
        DataAdapter2 unchangedAdapter2 = new DataAdapter2(MainActivity.this, arrayList);

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (stateMap.isEmpty() && costMap.isEmpty() && activityMap.isEmpty()) {
                    if (num == 1) {

                        unchangedAdapter.notifyDataSetChanged();
                        rv.setAdapter(unchangedAdapter);
                        dialog.dismiss();
                    } else {

                        unchangedAdapter2.notifyDataSetChanged();
                        rv.setAdapter(unchangedAdapter2);
                        dialog.dismiss();
                    }


                }else if(costMap.isEmpty() || stateMap.isEmpty() || activityMap.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please tick a filter from each category", Toast.LENGTH_SHORT).show();
                }
                else{
                    ArrayList<Locations> validList = new ArrayList<>();

                    for(Locations loc : arrayList){
                        if (loc.getCost().equals("Varies") || costMap.containsKey(loc.getCost())) {
                            if (loc.getState().equals("Outdoors & Indoors") || stateMap.containsKey(loc.getState())) {
                                for (String activity : loc.getActivities()) {
                                    if (activityMap.containsKey(activity)) {
                                        validList.add(loc);
                                        break;
                                    }
                                }
                            }

                        }
                    }
                    if (num == 1) {
                        FSDataAdapter adapter = new FSDataAdapter(MainActivity.this, validList);
                        adapter.notifyDataSetChanged();
                        rv.setAdapter(adapter);

                    } else {
                        DataAdapter2 adapter = new DataAdapter2(MainActivity.this, validList);
                        adapter.notifyDataSetChanged();
                        rv.setAdapter(adapter);
                    }

                    clearAllMaps();
                    Toast.makeText(MainActivity.this, "Filter applied", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        selectAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCostMap(btn1, 1);
                addToCostMap(btn2, 2);
                addToCostMap(btn3, 3);
                addToCostMap(btn9, 9);
            }
        });

        selectAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMap(stateMap, btn4);
                addToMap(stateMap, btn5);

            }
        });

        selectAll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMap(activityMap, btn6);
                addToMap(activityMap, btn7);
                addToMap(activityMap, btn8);

            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCostMap(btn1, 1);

            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCostMap(btn2, 2);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCostMap(btn3, 3);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMap(stateMap, btn4);

            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMap(stateMap, btn5);

            }
        });


        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMap(activityMap, btn6);

            }
        });

        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMap(activityMap, btn7);

            }
        });

        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToMap(activityMap, btn8);

            }
        });

        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCostMap(btn9, 9);
            }
        });


    }

    private void addToMap(HashMap<String, Boolean> map, Button button) {
        if (button.isSelected()) {
            button.setSelected(false);
            map.remove((String) button.getText());
        } else {
            button.setSelected(true);
            map.put((String) button.getText(), true);
        }
    }

    private void addToCostMap(Button button, int btnNum) {
        if (button.isSelected()) {
            button.setSelected(false);
            if (btnNum == 1) {
                costMap.remove("Low");
            } else if (btnNum == 2) {
                costMap.remove("Medium");
            } else if (btnNum == 3) {
                costMap.remove("High");
            } else if (btnNum == 9) {
                costMap.remove("Free");
            }

        } else {
            button.setSelected(true);
            if (btnNum == 1) {
                costMap.put("Low", true);
            } else if (btnNum == 2) {
                costMap.put("Medium", true);
            } else if (btnNum == 3) {
                costMap.put("High", true);
            } else if (btnNum == 9) {
                costMap.put("Free", true);
            }
        }
    }


    private void clearAllMaps() {
        costMap.clear();
        stateMap.clear();
        activityMap.clear();
    }


}