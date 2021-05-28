package com.example.orbital_app;

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
    private ArrayList<Locations> locations = new ArrayList<>();


    public SearchAdapter(ArrayList<Locations> locations) {
        this.locations = locations;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //ViewGroup is the parent class of all layout classes (ie RelativeLayout etc)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        SearchViewHolder holder = new SearchViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        holder.name.setText(locations.get(position).getName());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.parent.getContext(), Activity2.class);
                i.putExtra("location", locations.get(position).getName());
                i.putExtra("image", locations.get(position).getImage());
                i.putExtra("rating", locations.get(position).getRating());
                holder.parent.getContext().startActivity(i);

            }
        });

    }



    @Override
    public int getItemCount() {
        return locations.size();
    }

    public void filterList(ArrayList<Locations> filteredList){
        locations = filteredList;
        notifyDataSetChanged();
    }



//    @Override
//    public Filter getFilter() {
//        return filter;
//    }
//
//    Filter filter = new Filter() {
//        private Object Collection;
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<Locations> filteredList = new ArrayList<>();
//
//            if(constraint.toString().isEmpty()){
//                filteredList.addAll(locationsAll);
//            }else{
//                for (Locations loc: locationsAll){
//                    if(loc.getName().toLowerCase().contains(constraint.toString().toLowerCase())){
//                        filteredList.add(loc);
//                    }
//                }
//            }
//
//            FilterResults filterRes = new FilterResults();
//            filterRes.values = filteredList;
//            return filterRes;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            locations.clear();
//            locations.addAll((Collection<? extends Locations>)results.values);
//            notifyDataSetChanged();
//        }
//    };

    public class SearchViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private CardView parent;
        SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.searchName); // need to call using itemView because we are not inside an Activity
            parent = itemView.findViewById(R.id.searchParent);



        }
    }
}