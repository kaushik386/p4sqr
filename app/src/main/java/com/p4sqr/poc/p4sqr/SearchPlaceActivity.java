package com.p4sqr.poc.p4sqr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.p4sqr.poc.p4sqr.Model.AllData;
import com.p4sqr.poc.p4sqr.Model.AutoCompleteResponse;
import com.p4sqr.poc.p4sqr.Model.Predictions;
import com.p4sqr.poc.p4sqr.Services.ApiClient;
import com.p4sqr.poc.p4sqr.Services.ApiService;
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

public class SearchPlaceActivity extends AppCompatActivity {

    private CompositeDisposable disposable = new CompositeDisposable();
    ConnectableObservable<List<Predictions>> suggestionObservable;
    private ApiService apiService;
    List<AllData> mAllData = new ArrayList<>();
    List<Predictions> mPredictions;
    EditText mSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);
        apiService = ApiClient.getClient(getApplicationContext()).create(ApiService.class);
//        suggestionObservable = getSuggestions("Paris", "geocode", Constants.APIKEY).replay();
       mSearch.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });

        disposable.add(suggestionObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Predictions>>() {
                    @Override
                    public void onNext(List<Predictions> predictions) {
                        mPredictions = predictions;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getCause();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
       /* disposable.add(suggestionObservable
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<Predictions>, ObservableSource<Predictions>>() {
                    @Override
                    public ObservableSource<Predictions> apply(List<Predictions> predictions) throws Exception {
                        return Observable.fromIterable(predictions);
                    }
                })
                .flatMap(new Function<Predictions, ObservableSource<AllData>>() {
                    @Override
                    public ObservableSource<AllData> apply(Predictions predictions) throws Exception {
                        return getAllData(predictions);
                    }
                })
                .subscribeWith(new DisposableObserver<AllData>() {


                    @Override
                    public void onNext(AllData allData) {
                        mAllData.add(allData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getCause();
                    }

                    @Override
                    public void onComplete() {

                    }
                })
        );*/


    }

   /* private Observable<AllData> getAllData(final Predictions predictions) {
        return apiService.getAllData(predictions.getPlaceID(), "geometry", Constants.APIKEY)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<AllData, AllData>() {
                    @Override
                    public AllData apply(AllData allData) throws Exception {
                        allData.setPredictions(predictions);
                        return allData;
                    }
                });*/
    }

   /* private Observable<List<Predictions>> getSuggestions(String aa, String type, String key) {
        return apiService.getAllUsers(aa, type, key)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<AutoCompleteResponse, List<Predictions>>() {
                    @Override
                    public List<Predictions> apply(AutoCompleteResponse autoCompleteResponse) throws Exception {
                        return autoCompleteResponse.getPredictions();
                    }
                });*/


