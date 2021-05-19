package com.example.orbital_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SpecificLocRVAdapter extends RecyclerView.Adapter<SpecificLocRVAdapter.ViewHolder> {
    private Locations location;
    private Context context;
    public SpecificLocRVAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //ViewGroup is the parent class of all layout classes (ie RelativeLayout etc)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_details, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpecificLocRVAdapter.ViewHolder holder, int position) {
        holder.txtName.setText(location.getName());

        Glide.with(context)
                .asBitmap()
                .load(location.getImageUrl())
                .into(holder.image);

    }



    @Override
    public int getItemCount() {
        return 1;
    }

    public void setLocations(Locations location) {
        this.location = location;
        notifyDataSetChanged(); // allows refreshing of recycler view with new data
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtName;
        private CardView parent;
        private ImageView image;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName); // need to call using itemView because we are not inside an Activity
            parent = itemView.findViewById(R.id.parent);
            image = itemView.findViewById(R.id.image);

        }
    }
}
