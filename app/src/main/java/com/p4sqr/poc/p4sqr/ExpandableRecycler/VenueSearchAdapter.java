package com.p4sqr.poc.p4sqr.ExpandableRecycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.p4sqr.poc.p4sqr.R;

import java.util.List;

public class VenueSearchAdapter extends RecyclerView.Adapter<VenueSearchAdapter.MyHolder> {


    Context context;
    List<VenueName> mVenueName;

    public void swap( List<VenueName> venueName)
    {
     mVenueName = venueName;
     notifyDataSetChanged();
    }

    public VenueSearchAdapter(Context context, List<VenueName> mVenueName) {
        this.context = context;
        this.mVenueName = mVenueName;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.venue_search_layout,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.mVenueTextView.setText(mVenueName.get(position).getName());
//        holder.mVenueDistanceTextView.setText(mVenueName.get(position).getDistance());
        VenueDetail venueDetail = mVenueName.get(position).getChild();
        holder.mVenueCategory.setText(venueDetail.getCategory());
        holder.mVenueCity.setText(venueDetail.getCity());
        holder.mVenueState.setText(venueDetail.getState());
        holder.mVenueCountry.setText(venueDetail.getCountry());
        holder.linearLayout.setVisibility(View.GONE);



    }

    @Override
    public int getItemCount() {
        return mVenueName.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mVenueTextView;
//        private TextView mVenueDistanceTextView;
        private TextView mVenueCategory;
        private TextView mVenueCity;
        private TextView mVenueState;
        private TextView mVenueCountry;
        private LinearLayout linearLayout;

        public MyHolder(View itemView) {
            super(itemView);
            mVenueTextView= itemView.findViewById(R.id.svenue_name);
//            mVenueDistanceTextView =itemView.findViewById(R.id.venue_distance);
            linearLayout = itemView.findViewById(R.id.schildlayout);
            mVenueCategory = itemView.findViewById(R.id.svenue_category);
            mVenueCity = itemView.findViewById(R.id.scity);
            mVenueState = itemView.findViewById(R.id.sstate);
            mVenueCountry = itemView.findViewById(R.id.scountry);
            mVenueTextView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.svenue_name)
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
