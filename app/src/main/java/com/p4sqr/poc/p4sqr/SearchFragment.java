package com.p4sqr.poc.p4sqr;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenueName;
import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenuePCAdapter;
import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenueSearchAdapter;
import com.p4sqr.poc.p4sqr.Services.Explore4Sqr;

import java.util.List;

public class SearchFragment extends Fragment {


    Button mLocationButton;
    Button mSearchButton;
    String query="";
    private String searchLocation="";
    EditText mSearchEditText;
    List<VenueName> venueNames;
    RecyclerView mSearchRV;
    private VenueSearchAdapter mVenueSearchAdapter;
    ProgressDialog progressDialog;
    OnLocationSearchButtonListner mOnLocationButtonListner;

     interface OnLocationSearchButtonListner {
         void callLocationDialog4Search();
    }
    public void setLocation(String string)
    {
        if(!string.equals("")) {

            searchLocation = string;
            mLocationButton.setText("Change Location");

                mSearchButton.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnLocationButtonListner =(OnLocationSearchButtonListner) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_search,container,false);
        mLocationButton =view.findViewById(R.id.location);
        mSearchButton = view.findViewById(R.id.search_btn);
        mSearchEditText =view.findViewById(R.id.search_frag);
        mSearchRV = view.findViewById(R.id.venue_search_rv);

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnLocationButtonListner.callLocationDialog4Search();
            }
        });
    mSearchButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            query = mSearchEditText.getText().toString().trim();
            if(!searchLocation.equals(""))
            {
            new Search().execute(searchLocation, query);
        }
        }
    });


        return view;
    }


    class Search extends AsyncTask<String,Void,List<VenueName>>
    {


        @Override
        protected List<VenueName> doInBackground(String... strings) {
            return new Explore4Sqr().getSearchResult(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(List<VenueName> venues) {
            super.onPostExecute(venueNames);
            venueNames = venues;
            mSearchRV.setLayoutManager(new LinearLayoutManager(getContext()));
            mSearchRV.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
            mVenueSearchAdapter = new VenueSearchAdapter(getContext(),venues);
            mSearchRV.setAdapter(mVenueSearchAdapter);
        }
    }
}
