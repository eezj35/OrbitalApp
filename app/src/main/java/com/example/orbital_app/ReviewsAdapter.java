package com.example.orbital_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    Context context;
    ArrayList<Reviews> list;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = user.getUid();
    private FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    private DatabaseReference upVoteCnt = rtdb.getReference("upVotes").child(currentUserId);
    private DatabaseReference userInfo = rtdb.getReference("user");
//    DatabaseReference refData = FirebaseDatabase.getInstance().getReference("user").child(user.getUid());
//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
//    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public ReviewsAdapter(Context context, ArrayList<Reviews> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reviews_list_item, parent, false);
        return new ReviewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewHolder holder, int position) {
        holder.user.setText(list.get(position).getUser());
        holder.rating.setRating(list.get(position).getRating());
        holder.review.setText(list.get(position).getReview());


        DatabaseReference specificUserInfo = userInfo.child(list.get(position).getUserID());
        specificUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfoName userInfoName = snapshot.getValue(UserInfoName.class);
                if (userInfoName.getImage() != null) {
                    Glide.with(context)
                            .asBitmap()
                            .load(userInfoName.getImage())
                            .into(holder.reviewProfilePic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.upvotes.setText(list.get(position).getUpVote() + "");
        holder.upvotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upVoteCnt.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String id = list.get(position).getId();
                        if (!snapshot.hasChild(id)) {
                            holder.upvotes.setText(Integer.parseInt(holder.upvotes.getText().toString()) + 1 + "");
                            db.collection("reviews").document(id).
                                    update("upVote", Integer.parseInt(holder.upvotes.getText().toString()));
                            upVoteCnt.child(id).setValue(true);
                        } else {
                            Toast.makeText(context, "You have already upvoted", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder{
        TextView user;
        RatingBar rating;
        TextView review;
        TextView upvotes;
        ImageButton upvotesBtn;
        CircleImageView reviewProfilePic;

        public ReviewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user);
            rating = itemView.findViewById(R.id.ratingReview);
            review = itemView.findViewById(R.id.review);
            upvotes = itemView.findViewById(R.id.upvotes);
            upvotesBtn = itemView.findViewById(R.id.upvoteBtn);
            reviewProfilePic = itemView.findViewById(R.id.dp);

        }
    }
}
