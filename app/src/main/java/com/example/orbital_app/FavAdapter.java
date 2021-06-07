package com.example.orbital_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> {

    Context context;
    ArrayList<Locations> list;

    public FavAdapter(Context context, ArrayList<Locations> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.fav_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.MyViewHolder holder, int position) {


        holder.name.setText(list.get(position).getName());

        holder.favCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.favCard.getContext(), Activity2.class);
                i.putExtra("location", list.get(position).getName());
                i.putExtra("image", list.get(position).getImage());
                i.putExtra("rating", list.get(position).getRating());
                i.putExtra("cost", list.get(position).getCost());
                i.putExtra("state", list.get(position).getState());
                i.putExtra("id", list.get(position).getId());
                i.putExtra("generalLoc", list.get(position).getGeneralLoc());
                i.putExtra("openingHours", list.get(position).getOpeningHours());
                i.putExtra("briefDsc", list.get(position).getBriefDsc());
                i.putExtra("link", list.get(position).getLink());
                holder.favCard.getContext().startActivity(i);

            }
        });

        Glide.with(context)
                .asBitmap()
                .load(list.get(position).getImage())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        CardView favCard;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            favCard = itemView.findViewById(R.id.favCard);
            name = itemView.findViewById(R.id.favName);
            image = itemView.findViewById(R.id.favPic);
        }

    }
}
