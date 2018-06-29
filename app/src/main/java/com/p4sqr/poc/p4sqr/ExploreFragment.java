package com.p4sqr.poc.p4sqr;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenueAdapter;
import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenueDetail;
import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenueName;
import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenuePCAdapter;
import com.p4sqr.poc.p4sqr.Model.SectionModel;
import com.p4sqr.poc.p4sqr.Model.VenueModel;
import com.p4sqr.poc.p4sqr.Services.Explore4Sqr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class ExploreFragment extends Fragment implements SectionAdapter.ClickEvent {


    Button mLocation;
    RecyclerView mRVSection;
    RecyclerView mVenueRV;
    List<SectionModel>  mSection ;
    private List<VenueName> mVenueName;
    OnLocationButtonListner mCallBack;
    private SectionAdapter mSectionAdapter;

 void setSectionModel()
        {
            mSection = new ArrayList<>();
            String[] string = {"Food", "Drinks", "Coffee", "Shops", "Arts", "Outdoors", "Sights", "Trending" } ;
            for(String temp:string)
            {
               SectionModel sectionModel= new SectionModel(temp,false);
                mSection.add(sectionModel);
            }
        }

    @Override
    public void sendBack(int adapterPosition) {
     setSectionModel();
     mSection.get(adapterPosition).setChecked(true);
     mSectionAdapter.swap(mSection);
    }

    public interface OnLocationButtonListner {
        public void callLocationDialog();
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
        mRVSection = view.findViewById(R.id.rvsection);
        mVenueRV = view.findViewById(R.id.venue_rv);
        setSectionRV();
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//        mCallBack.callLocationDialog();
                new Explore4SQR().execute("13,77.59");
            }
        });

        return view;
    }

    void setSectionRV()
    {
        setSectionModel();
        mSectionAdapter=new SectionAdapter(getContext(),this,mSection);
        mRVSection.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true));
        mRVSection.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        mRVSection.setAdapter(mSectionAdapter);
    }

    class Explore4SQR extends AsyncTask<String,Void,List<VenueName>>
    {
        @Override
        protected List<VenueName> doInBackground(String... strings) {
            return new Explore4Sqr().fetchItems(strings[0]);
        }

        @Override
        protected void onPostExecute(List<VenueName> venueName) {
           mVenueName = venueName;
            mVenueRV.setLayoutManager(new LinearLayoutManager(getContext()));
            mVenueRV.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
            mVenueRV.setAdapter(new VenuePCAdapter(getContext(),venueName));

        }
    }


}
