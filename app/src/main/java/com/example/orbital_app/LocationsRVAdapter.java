package com.example.orbital_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class LocationsRVAdapter extends RecyclerView.Adapter<LocationsRVAdapter.ViewHolder> {
    private ArrayList<Locations> locations = new ArrayList<>();
    private Context context;
    public LocationsRVAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //ViewGroup is the parent class of all layout classes (ie RelativeLayout etc)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsRVAdapter.ViewHolder holder, int position) {
        holder.txtName.setText(locations.get(position).getName());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.parent.getContext(), Activity2.class);
                i.putExtra("location", locations.get(position).getName());
                i.putExtra("image", locations.get(position).getImage());
                holder.parent.getContext().startActivity(i);

            }
        });

        Glide.with(context)
                .asBitmap()
                .load(locations.get(position).getImage())
                .into(holder.image);

    }



    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void setLocations(ArrayList<Locations> locations) {
        this.locations = locations;
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
