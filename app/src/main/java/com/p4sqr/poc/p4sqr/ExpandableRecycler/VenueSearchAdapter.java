package com.p4sqr.poc.p4sqr.ExpandableRecycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p4sqr.poc.p4sqr.Model.Category;
import com.p4sqr.poc.p4sqr.Model.Venue;
import com.p4sqr.poc.p4sqr.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VenueSearchAdapter extends RecyclerView.Adapter<VenueSearchAdapter.MyHolder> {


    Context context;
    List<Venue> mVenues;

    public void swap( List<Venue> venueName)
    {
     mVenues = venueName;
        notifyDataSetChanged();
    }

    public VenueSearchAdapter(Context context, List<Venue> mVenueName) {
        this.context = context;
        this.mVenues = mVenueName;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.venue_search_layout,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Venue venue = mVenues.get(position);
        String imageUrl = venue.getPrefix()+"1200x500"+venue.getSuffix();
        holder.mVenueTextView.setText(venue.getName());
//        holder.mVenueDistanceTextView.setText(mVenueName.get(position).getDistance());
        Picasso.with(context).load(imageUrl).into(holder.imageSearch);
        Category category= venue.getCategories().get(0);
        holder.mVenueCategory.setText(category.getName());
        holder.mVenueCity.setText(venue.getLocation().getCity());
        holder.mVenueState.setText(venue.getLocation().getState());
        holder.mVenueCountry.setText(venue.getLocation().getCountry());
        holder.linearLayout.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return mVenues.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mVenueTextView;
//        private TextView mVenueDistanceTextView;
        private TextView mVenueCategory;
        private TextView mVenueCity;
        private TextView mVenueState;
        private TextView mVenueCountry;
        private LinearLayout linearLayout;
        ImageView imageSearch;

        public MyHolder(View itemView) {
            super(itemView);
            imageSearch = itemView.findViewById(R.id.image_search);
            mVenueTextView= itemView.findViewById(R.id.svenue_name);
            linearLayout = itemView.findViewById(R.id.schildlayout);
            mVenueCategory = itemView.findViewById(R.id.svenue_category);
            mVenueCity = itemView.findViewById(R.id.scity);
            mVenueState = itemView.findViewById(R.id.sstate);
            mVenueCountry = itemView.findViewById(R.id.scountry);
            mVenueTextView.setOnClickListener(this);
            imageSearch.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.svenue_name||v.getId()==R.id.image_search)
            {
                if (linearLayout.getVisibility() == View.VISIBLE) {
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
            }
            }

        }
    }
}
