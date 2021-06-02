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

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    Context context;
    ArrayList<Reviews> list;

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
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder{
        TextView name;
        CardView parent;
        ImageView image;
        public ReviewHolder(View itemView) {
            super(itemView);

        }
    }
}
