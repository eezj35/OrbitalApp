package com.example.orbital_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private ArrayList<Locations> list = new ArrayList<>();


    public SearchAdapter(ArrayList<Locations> locations) {
        this.list = locations;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //ViewGroup is the parent class of all layout classes (ie RelativeLayout etc)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        SearchViewHolder holder = new SearchViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
                i.putExtra("prevPage", "Search");
                i.putExtra("exactCost", list.get(position).getExactCost());
                holder.parent.getContext().startActivity(i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(ArrayList<Locations> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private CardView parent;
        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.searchName);
            parent = itemView.findViewById(R.id.searchParent);

        }
    }
}
