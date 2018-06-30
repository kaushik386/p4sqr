package com.p4sqr.poc.p4sqr;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenueName;
import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenuePCAdapter;
import com.p4sqr.poc.p4sqr.Model.SectionModel;
import com.p4sqr.poc.p4sqr.Services.Explore4Sqr;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment implements SectionAdapter.ClickEvent {


    Button mLocation;
    Button mSearch;
    RecyclerView mRVSection;
    RecyclerView mVenueRV;
    List<SectionModel>  mSection ;
    OnLocationButtonListner mCallBack;
    private List<VenueName> mVenueName;
    private SectionAdapter mSectionAdapter;
    private String searchLocation="";
    private String section ="";
    private VenuePCAdapter venuePCAdapter;

    public void setLocation(String string)
    {
        if(!string.equals("")) {

            searchLocation = string;
            mLocation.setText("Change Location");
//            mLocation.setScrollBarStyle(getActivity);
            if(!section.equals(""))
            {
                mSearch.setVisibility(View.VISIBLE);
            }
        }
    }

 void setSectionModel()
        {
            mSection = new ArrayList<>();
            String[] string = {"Food", "Drinks", "Coffee", "Shops", "Arts", "Outdoors", "Sights", "Trending" } ;
            for(String temp:string)
            {
               SectionModel sectionModel= new SectionModel(temp.toUpperCase(),false);
                mSection.add(sectionModel);
            }
        }

        void searchButtonClicked()
        {
            if(mSearch.getText().equals("Reset"))
            {
                mSearch.setText("Search");
                setSectionModel();
                mSectionAdapter.swap(mSection);
                mLocation.setText("Select Location");
                venuePCAdapter.swap(new ArrayList<VenueName>());
                mSearch.setVisibility(View.GONE);
                searchLocation="";
                section="";

            }
            else {
                if (!searchLocation.equals("") && !section.equals("")) {
                    new Explore4SQR().execute(searchLocation, section.toLowerCase());
                    mSearch.setText("Reset");

                }
            }

        }

    @Override
    public void sendBack(int adapterPosition) {
     setSectionModel();
     mSection.get(adapterPosition).setChecked(true);
     mSectionAdapter.swap(mSection);
     section = mSection.get(adapterPosition).getmName();
     if(!searchLocation.equals("")) {
         mSearch.setVisibility(View.VISIBLE);
     }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
        mCallBack =(OnLocationButtonListner) context;
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

        View view = inflater.inflate(R.layout.fragment_explore,container,false);
        mLocation = view.findViewById(R.id.location);
        mSearch = view.findViewById(R.id.search);
        mRVSection = view.findViewById(R.id.rvsection);
        mVenueRV = view.findViewById(R.id.venue_rv);
        setSectionRV();
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        mCallBack.callLocationDialog();

            }
        });
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButtonClicked();
            }
        });

        return view;
    }

    void setSectionRV()
    {
        setSectionModel();
        mSectionAdapter=new SectionAdapter(getContext(),this,mSection);
        mRVSection.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mRVSection.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        mRVSection.setAdapter(mSectionAdapter);
    }

     interface OnLocationButtonListner {
         void callLocationDialog();
    }

    class Explore4SQR extends AsyncTask<String,Void,List<VenueName>>
    {
        @Override
        protected List<VenueName> doInBackground(String... strings) {
            return new Explore4Sqr().getExploreResult(strings[0],strings[1]);
        }

        @Override
        protected void onPostExecute(List<VenueName> venueName) {
           mVenueName = venueName;
            mVenueRV.setLayoutManager(new LinearLayoutManager(getContext()));
            mVenueRV.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
            venuePCAdapter = new VenuePCAdapter(getContext(),venueName);
            mVenueRV.setAdapter(venuePCAdapter);

        }
    }


}
