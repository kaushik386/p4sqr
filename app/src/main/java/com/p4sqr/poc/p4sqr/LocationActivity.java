package com.p4sqr.poc.p4sqr;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.p4sqr.poc.p4sqr.Model.LocationModel;
import com.p4sqr.poc.p4sqr.Services.Explore4Sqr;
import com.p4sqr.poc.p4sqr.Services.FetchPlaceInfo;


public class LocationActivity extends AppCompatActivity {


    private static final int REQUEST_CODE = 1000;
    Button mDetectCurrentLoca;
    Button mSearchButton;
    EditText mInputText;
    ProgressDialog progressDoalog;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LocationModel mLocationModel;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mDetectCurrentLoca = (Button) findViewById(R.id.detect_current_loca);
        mSearchButton = (Button) findViewById(R.id.search);
        mInputText = (EditText) findViewById(R.id.search_text);

        if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            buildLocationRequest();
            buildLocationCallBack();
        }
        progressDoalog = new ProgressDialog(LocationActivity.this);
        progressDoalog.setMessage("Loading....");

        mDetectCurrentLoca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

                    return;
                }
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


//              *//*  Call<List<LocationModel>> call = getPlacesRecommendation.getAllphotos();
//                call.enqueue(new Callback<List<LocationModel>>() {
//                    @Override
//                    public void onResponse(Call<List<LocationModel>> call, Response<List<LocationModel>> response) {
//                        progressDoalog.dismiss();
//                        response.body();
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<LocationModel>> call, Throwable t) {
//                        progressDoalog.dismiss();
//                        Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
//
//                    }
//                });*//*
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String placeName = mInputText.getText().toString();
                if (placeName.equals("")) {
                    Toast.makeText(LocationActivity.this, "Place is empty", Toast.LENGTH_LONG).show();
                } else {


                }
            }
        });


    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    sendBackResult(location.getLatitude(), location.getLongitude());
                }
            }
        };


    }

    private void buildLocationRequest() {


        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    void sendBackResult(double latitude, double longitude) {
        Intent intent = new Intent();
        intent.putExtra("longitude", longitude);
        intent.putExtra("latitude", latitude);
        setResult(Activity.RESULT_OK, intent);
        if (mFusedLocationClient != null) {
            removeFusedLocationClient();
        }
        finish();

    }

    private void removeFusedLocationClient() {
        if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }



}
