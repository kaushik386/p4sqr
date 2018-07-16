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
import com.p4sqr.poc.p4sqr.Model.Items;
import com.p4sqr.poc.p4sqr.Model.Venue;
import com.p4sqr.poc.p4sqr.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VenuePCAdapter extends RecyclerView.Adapter<VenuePCAdapter.MyHolder> {


    Context context;
    List<Items> mItems;

    public void swap( List<Items> venueName)
    {
        mItems = venueName;
     notifyDataSetChanged();
    }

    public VenuePCAdapter(Context context,List<Items> items) {
        this.context = context;
        this.mItems = items;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.venue_layout,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {




        Venue venue = mItems.get(position).getVenues();
        String imageUrl = venue.getPrefix()+"1200x500"+venue.getSuffix();
                holder.mVenueTextView.setText(venue.getName());
//        holder.mVenueDistanceTextView.setText(mVenueName.get(position).getDistance());
        Picasso.with(context).load(imageUrl).into(holder.mImage);
         Category category= venue.getCategories().get(0);
        holder.mVenueCategory.setText(category.getName());
        holder.mVenueCity.setText(venue.getLocation().getCity());
        holder.mVenueState.setText(venue.getLocation().getState());
        holder.mVenueCountry.setText(venue.getLocation().getCountry());
        holder.linearLayout.setVisibility(View.GONE);



    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImage;
        private TextView mVenueTextView;
//        private TextView mVenueDistanceTextView;
        private TextView mVenueCategory;
        private TextView mVenueCity;
        private TextView mVenueState;
        private TextView mVenueCountry;
        private LinearLayout linearLayout;

        public MyHolder(View itemView) {
            super(itemView);
            mVenueTextView= itemView.findViewById(R.id.venue_name);
            mImage = itemView.findViewById(R.id.image_view);
//            mVenueDistanceTextView =itemView.findViewById(R.id.venue_distance);
            linearLayout = itemView.findViewById(R.id.childlayout);
            mVenueCategory = itemView.findViewById(R.id.venue_category);
            mVenueCity = itemView.findViewById(R.id.city);
            mVenueState = itemView.findViewById(R.id.state);
            mVenueCountry = itemView.findViewById(R.id.country);
            mVenueTextView.setOnClickListener(this);
            mImage.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.venue_name || v.getId()==R.id.image_view)
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
