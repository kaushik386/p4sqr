package com.p4sqr.poc.p4sqr;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.p4sqr.poc.p4sqr.Model.LocationModel;
import com.p4sqr.poc.p4sqr.Services.FetchPlaceInfo;

public class DialogLocationFragment extends DialogFragment {


    private static final int REQUEST_CODE = 1000;
    Button mDetectCurrentLoca;
    Button mSearchButton;
    EditText mInputText;
    private PassBackToMainActivity mCallBack;
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
                            if(mFusedLocationClient==null)
                        {
                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                        buildLocationRequest();
                        buildLocationCallBack();
                        }
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallBack =(PassBackToMainActivity) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);

        mDetectCurrentLoca = (Button) v.findViewById(R.id.detect_current_loca);
        mSearchButton = (Button) v.findViewById(R.id.search);
        mInputText = (EditText) v.findViewById(R.id.search_text);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            buildLocationRequest();
            buildLocationCallBack();
        }

        mDetectCurrentLoca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

                    return;
                }
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String placeName = mInputText.getText().toString();
                if (placeName.equals("")) {
                    Toast.makeText(getActivity(), "Place is empty", Toast.LENGTH_LONG).show();
                } else {

                    new fetchplace().execute(placeName);
                }
            }
        });


        return v;
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                   passLL(location.getLatitude(),location.getLongitude());
                }
            }
        };


    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    private void buildLocationRequest() {


        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

private void passLL(double latitude, double longtitude)
{
    String temp = String.valueOf(latitude)+","+String.valueOf(longtitude);
    mCallBack.SendBackData(temp);
    if (mFusedLocationClient != null) {
        removeFusedLocationClient();
    }
    this.dismiss();

}
    private void removeFusedLocationClient() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }
    interface PassBackToMainActivity{
        void SendBackData(String string);

    }

    private class fetchplace extends AsyncTask<String, Void, LocationModel> {

        @Override
        protected LocationModel doInBackground(String... strings) {
            return new FetchPlaceInfo().fetchItems(strings[0]);
        }

        @Override
        protected void onPostExecute(LocationModel locationModel) {
            mLocationModel = locationModel;
            passLL(locationModel.getLatitude(), locationModel.getLongitude());
        }
    }

}
