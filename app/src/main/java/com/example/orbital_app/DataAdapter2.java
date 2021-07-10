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

import java.util.ArrayList;

public class DataAdapter2 extends RecyclerView.Adapter<DataAdapter2.MyViewHolder> {

    Context context;
    ArrayList<Locations> list;

    public DataAdapter2(Context context, ArrayList<Locations> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.locations_list_item_2, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter2.MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getName());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.parent.getContext(), Activity2.class);
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
                i.putExtra("postal", list.get(position).getPostal());
                i.putExtra("activities", list.get(position).getActivities());
                i.putExtra("prevPage", "MA");
                i.putExtra("exactCost", list.get(position).getExactCost());
                holder.parent.getContext().startActivity(i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

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

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CardView parent;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent2);
            name = itemView.findViewById(R.id.txtName2);
            image = itemView.findViewById(R.id.image2);
        }
    }
}
