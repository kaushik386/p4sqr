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
import com.p4sqr.poc.p4sqr.Model.FourSquareResponse;
import com.p4sqr.poc.p4sqr.Model.Items;
import com.p4sqr.poc.p4sqr.Model.SectionModel;
import com.p4sqr.poc.p4sqr.Model.VenuePhoto.VenuePhotoResponse;
import com.p4sqr.poc.p4sqr.Services.ApiClient;
import com.p4sqr.poc.p4sqr.Services.ApiService;
import com.p4sqr.poc.p4sqr.Services.Explore4Sqr;
import com.p4sqr.poc.p4sqr.app.Constants;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ExploreFragment extends Fragment implements SectionAdapter.ClickEvent {


    Button mLocation;
    Button mSearch;
    RecyclerView mRVSection;
    RecyclerView mVenueRV;
    List<SectionModel> mSection;
    OnLocationButtonListner mCallBack;
    private List<VenueName> mVenueName;
    private ApiService apiService;
    private SectionAdapter mSectionAdapter;
    private String searchLocation = "";
    private String section = "";
    private VenuePCAdapter venuePCAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    ConnectableObservable<List<Items>> suggestionObservable;

    public void setLocation(String string) {
        if (!string.equals("")) {

            searchLocation = string;
            mLocation.setText("Change Location");
//            mLocation.setScrollBarStyle(getActivity);
            if (!section.equals("")) {
                mSearch.setVisibility(View.VISIBLE);
            }
        }
    }

    void setSectionModel() {
        mSection = new ArrayList<>();
        String[] string = {"Food", "Drinks", "Coffee", "Shops", "Arts", "Outdoors", "Sights", "Trending"};
        for (String temp : string) {
            SectionModel sectionModel = new SectionModel(temp.toUpperCase(), false);
            mSection.add(sectionModel);
        }
    }

    void searchButtonClicked() {
        if (mSearch.getText().equals("Reset")) {
            mSearch.setText("Search");
            setSectionModel();
            mSectionAdapter.swap(mSection);
            mLocation.setText("Select Location");
            venuePCAdapter.swap(new ArrayList<Items>());
            mSearch.setVisibility(View.GONE);
            searchLocation = "";
            section = "";

        } else {
            if (!searchLocation.equals("") && !section.equals("")) {

                suggestionObservable = getVenues(Constants.CLIENT_ID, Constants.CLIENT_SECRET, "20180626", searchLocation, section, "20").replay();
                disposable.add(suggestionObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Items>>() {

                            @Override
                            public void onNext(List<Items> items) {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }));
                final List<Items> mItems = new ArrayList<>();
                disposable.add(suggestionObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new Function<List<Items>, ObservableSource<Items>>() {
                            @Override
                            public ObservableSource<Items> apply(List<Items> items) throws Exception {
                                return Observable.fromIterable(items);
                            }
                        }).flatMap(new Function<Items, ObservableSource<Items>>() {
                            @Override
                            public ObservableSource<Items> apply(Items items) throws Exception {
                                return getAllVenueDetails(Constants.CLIENT_ID, Constants.CLIENT_SECRET, "20180626",items);
                            }
                        })
                        .subscribeWith(new DisposableObserver<Items>() {
                            @Override
                            public void onNext(Items items) {
                                mItems.add(items);
                            }

                            @Override
                            public void onError(Throwable e) {
                            e.getCause();
                            }

                            @Override
                            public void onComplete() {


                                mVenueRV.setLayoutManager(new LinearLayoutManager(getContext()));
                                mVenueRV.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                                venuePCAdapter = new VenuePCAdapter(getContext(), mItems);
                                mVenueRV.setAdapter(venuePCAdapter);
                            }
                        }))
                ;
                suggestionObservable.connect();
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
        if (!searchLocation.equals("")) {
            mSearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallBack = (OnLocationButtonListner) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        mLocation = view.findViewById(R.id.location);
        mSearch = view.findViewById(R.id.search);
        mRVSection = view.findViewById(R.id.rvsection);
        mVenueRV = view.findViewById(R.id.venue_rv);
        apiService = ApiClient.getClient(getActivity().getApplicationContext()).create(ApiService.class);
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

    void setSectionRV() {
        setSectionModel();
        mSectionAdapter = new SectionAdapter(getContext(), this, mSection);
        mRVSection.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRVSection.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL));
        mRVSection.setAdapter(mSectionAdapter);
    }

    interface OnLocationButtonListner {
        void callLocationDialog();
    }



    private Observable<List<Items>> getVenues(String client_id, String client_code, String date, String ll, String section, String limit) {
        return apiService.getExploreResult(client_id, client_code, date, ll, section, limit).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toObservable()
                .map(new Function<FourSquareResponse, List<Items>>() {
                    @Override
                    public List<Items> apply(FourSquareResponse fourSquareResponse) {
                        return fourSquareResponse.getResponse().getGroups().get(0).getItems();
                    }
                })
                ;
    }

    private Observable<Items> getAllVenueDetails(String client_id, String client_code, String date,final Items item) {
        return apiService.getVenueDetails(item.getVenues().getId(), client_id,client_code,date,"1")
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<VenuePhotoResponse, Items>() {
                    @Override
                    public Items apply(VenuePhotoResponse venuePhotoResponse) throws Exception {
                        item.getVenues().setPrefix(venuePhotoResponse.getResponse().getPhotos().getItems().get(0).getPrefix());
                        item.getVenues().setSuffix(venuePhotoResponse.getResponse().getPhotos().getItems().get(0).getSuffix());
                        return item;
                    }

                });
    }

}
