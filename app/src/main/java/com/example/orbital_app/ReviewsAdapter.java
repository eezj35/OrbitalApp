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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    private Context context;
    private ArrayList<Reviews> list;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String currentUserId = user.getUid();
    private FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
    private DatabaseReference upVoteCnt = rtdb.getReference("upVotes").child(currentUserId);
    private DatabaseReference userInfo = rtdb.getReference("user");


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

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.rating.setRating(list.get(position).getRating());
        holder.review.setText(list.get(position).getReview());
        holder.upvotes.setText(list.get(position).getUpVote() + "");


        holder.upvotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = list.get(position).getId();
                upVoteCnt.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (!snapshot.hasChild(id)) {
                            Integer number = Integer.parseInt(holder.upvotes.getText().toString()) + 1;

                            db.collection("reviews").document(id).
                                    update("upVote", number);
                            upVoteCnt.child(id).setValue(true);
                            DocumentReference docRef = db.collection("reviews").document(id);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document != null) {
                                            holder.upvotes.setText(document.get("upVote").toString());
                                        }
                                    }
                                }
                            });


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


        DatabaseReference specificUserInfo = userInfo.child(list.get(position).getUserID());
        specificUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfoName userInfoName = snapshot.getValue(UserInfoName.class);
                holder.user.setText(userInfoName.getUserName());
                if (userInfoName.getImage() != null) {
                    Glide.with(context)
                            .asBitmap()
                            .load(userInfoName.getImage())
                            .into(holder.reviewProfilePic);
                } else {
                    holder.reviewProfilePic.setImageResource(R.drawable.ic_profile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            upvotes = itemView.findViewById(R.id.upvotesCnt);
            upvotesBtn = itemView.findViewById(R.id.upvoteBtn);
            reviewProfilePic = itemView.findViewById(R.id.dp);

        }
    }
}
