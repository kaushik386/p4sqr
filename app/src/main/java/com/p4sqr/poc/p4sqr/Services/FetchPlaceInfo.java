package com.p4sqr.poc.p4sqr.Services;

import android.net.Uri;
import android.util.Log;

import com.p4sqr.poc.p4sqr.Model.LocationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FetchPlaceInfo {

    private static final String TAG = "fecthPlaceInfo";
    private static final String APIKEY= "AIzaSyD2QSy0wXedfSvQHj4mccxgyv8G7UIkJcM";
    private LocationModel locationModel;

    public byte[] getUrlBytes(String urlSpec) throws IOException
    {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);

            }
            out.close();

            return out.toByteArray();

        }
        finally {
            connection.disconnect();
        }
    }


    public String getUrlString(String UrlSpec) throws IOException
    {
        return new String(getUrlBytes(UrlSpec));
    }


    public LocationModel fetchItems(String place) {
        try{
            String url = Uri.parse("https://maps.googleapis.com/maps/api/place/findplacefromtext/json")
                    .buildUpon()
                    .appendQueryParameter("input",place)
                    .appendQueryParameter("inputtype","textquery")
                    .appendQueryParameter("fields","name,geometry")
                    .appendQueryParameter("key",APIKEY)
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONObject JsonBody = new JSONObject(jsonString);
            parseItems(JsonBody);

        }
        catch(IOException ioe)
        {
            Log.e(TAG,"Failed to fetch items",ioe);
        }
        catch (JSONException js)
        {
            Log.e(TAG,"exception",js);
        }
        return locationModel;
    }

    public void parseItems( JSONObject jsonBody) throws IOException,JSONException
    {
        String status = jsonBody.getString("status");
        locationModel = new LocationModel();
        if(status.equals("OK")) {
            JSONArray candidatesJsonArray = jsonBody.getJSONArray("candidates");
            JSONObject temp  = candidatesJsonArray.getJSONObject(0);
            locationModel.setPlace(temp.getString("name"));
            JSONObject geometry = temp.getJSONObject("geometry");
            JSONObject location = geometry.getJSONObject("location");
            locationModel.setLatitude(location.getDouble("lat"));
            locationModel.setLongitude(location.getDouble("lng"));
            }
    }



}
