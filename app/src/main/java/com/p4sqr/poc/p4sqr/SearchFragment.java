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
import com.p4sqr.poc.p4sqr.Model.FourSquareResponse;
import com.p4sqr.poc.p4sqr.Model.Items;
import com.p4sqr.poc.p4sqr.Model.Venue;
import com.p4sqr.poc.p4sqr.Model.VenuePhoto.VenuePhotoResponse;
import com.p4sqr.poc.p4sqr.Model.VenueSearch.VenueSearchResponse;
import com.p4sqr.poc.p4sqr.Services.ApiClient;
import com.p4sqr.poc.p4sqr.Services.ApiService;
import com.p4sqr.poc.p4sqr.Services.Explore4Sqr;
import com.p4sqr.poc.p4sqr.app.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchFragment extends Fragment {


    Button mLocationButton;
    Button mSearchButton;
    String query = "";
    private String searchLocation = "";
    EditText mSearchEditText;
    List<VenueName> venueNames;
    RecyclerView mSearchRV;
    private VenueSearchAdapter mVenueSearchAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    ConnectableObservable<List<Venue>> searchObservable;
    private ApiService apiService;
    ProgressDialog progressDialog;
    OnLocationSearchButtonListner mOnLocationButtonListner;

    interface OnLocationSearchButtonListner {
        void callLocationDialog4Search();
    }

    public void setLocation(String string) {
        if (!string.equals("")) {

            searchLocation = string;
            mLocationButton.setText("Change Location");

            mSearchButton.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnLocationButtonListner = (OnLocationSearchButtonListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mLocationButton = view.findViewById(R.id.location);
        mSearchButton = view.findViewById(R.id.search_btn);
        mSearchEditText = view.findViewById(R.id.search_frag);
        mSearchRV = view.findViewById(R.id.venue_search_recycle_View);
        mSearchRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mSearchRV.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        apiService = ApiClient.getClient(getActivity().getApplicationContext()).create(ApiService.class);
        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnLocationButtonListner.callLocationDialog4Search();
            }
        });
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchSearchResult();
            }
        });


        return view;
    }


 void fetchSearchResult()
 {

     if(mVenueSearchAdapter!=null)
     {
         mVenueSearchAdapter.swap(new ArrayList<Venue>());
     }

     query = mSearchEditText.getText().toString();
     if (!searchLocation.equals("")) {

//            new Search().execute(searchLocation, query);
         searchObservable = getVenues(Constants.CLIENT_ID, Constants.CLIENT_SECRET, "20180626", searchLocation, query, "10").replay();

         disposable.add(searchObservable.subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribeWith(new DisposableObserver<List<Venue>>() {
                     @Override
                     public void onNext(List<Venue> venues) {

                     }

                     @Override
                     public void onError(Throwable e) {
                         e.getCause();
                     }

                     @Override
                     public void onComplete() {

                     }
                 }));
         final List<Venue> venueList = new ArrayList<>();
         disposable.add(searchObservable.subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .flatMap(new Function<List<Venue>, ObservableSource<Venue>>() {
                     @Override
                     public ObservableSource<Venue> apply(List<Venue> venues) throws Exception {
                         return Observable.fromIterable(venues);
                     }
                 })
                 .flatMap(new Function<Venue, ObservableSource<Venue>>() {
                     @Override
                     public ObservableSource<Venue> apply(Venue venue) throws Exception {
                         return getAllVenueDetails(Constants.CLIENT_ID, Constants.CLIENT_SECRET, "20180626", venue);
                     }
                 })
                 .subscribeWith(new DisposableObserver<Venue>() {
                     @Override
                     public void onNext(Venue venue) {
                         venueList.add(venue);
                     }

                     @Override
                     public void onError(Throwable e) {
                         e.getCause();
                     }

                     @Override
                     public void onComplete() {
                         if(mVenueSearchAdapter!=null)
                         {
                             mVenueSearchAdapter.swap(venueList);
                         }
                         else {
                             mVenueSearchAdapter = new VenueSearchAdapter(getContext(), venueList);
                             mSearchRV.setAdapter(mVenueSearchAdapter);
                         }
                     }
                 }));
         searchObservable.connect();
     }




 }

    private Observable<List<Venue>> getVenues(String client_id, String client_code, String date, String ll, String query, String limit) {
        return apiService.getSearchResult(client_id, client_code, date, ll, query, limit).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toObservable()
                .map(new Function<VenueSearchResponse, List<Venue>>() {
                    @Override
                    public List<Venue> apply(VenueSearchResponse venueSearchResponse) throws Exception {
                        return venueSearchResponse.getSearchResponse().getVenue();
                    }
                })

                ;
    }

    private Observable<Venue> getAllVenueDetails(String client_id, String client_code, String date, final Venue venue) {
        return apiService.getVenuePictures(venue.getId(), client_id, client_code, date, "1")
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(new Function<VenuePhotoResponse, Venue>() {
                    @Override
                    public Venue apply(VenuePhotoResponse venuePhotoResponse) throws Exception {
                        if(venuePhotoResponse.getResponse().getPhotos().getItems()!=null) {
                            if (venuePhotoResponse.getResponse().getPhotos().getItems().get(0).getSuffix() != null && venuePhotoResponse.getResponse().getPhotos().getItems().get(0).getPrefix() != null) {
                                venue.setPrefix(venuePhotoResponse.getResponse().getPhotos().getItems().get(0).getPrefix());
                                venue.setSuffix(venuePhotoResponse.getResponse().getPhotos().getItems().get(0).getSuffix());
                            }
                        }
                        else
                        {
                            venue.setPrefix("https://igx.4sqi.net/img/general/");
                            venue.setSuffix("/leeON5zzFupWAe2cD-Po4jn-nQrhZ4kdEu42OIhOGAw.jpg");
                        }
//
                        return venue;
                    }

                });
    }
}
