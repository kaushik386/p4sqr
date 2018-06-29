package com.p4sqr.poc.p4sqr.Services;

import android.net.Uri;
import android.util.Log;

import com.google.gson.stream.JsonWriter;
import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenueDetail;
import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenueName;
import com.p4sqr.poc.p4sqr.Model.VenueModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Explore4Sqr {

    private static final String TAG = "explore4sqr";
    private static final String CLIENT_SECRET = "LELVVTEIZKORS3TOTQX3HPZ2K444MBARDJQWWJ0VXKSG3GER";
    private static final String CLIENT_ID = "XJ3P55MU225A20H33YQ0XXOVHHR0FHGLTLBQBKVATEYDSNY0";
    private List<VenueName> mVenueName =new ArrayList<>();



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
    public List<VenueName> fetchItems(String ll) {
        try{
            String url = Uri.parse("https://api.foursquare.com/v2/venues/explore")
                    .buildUpon()
                    .appendQueryParameter("client_id",CLIENT_ID)
                    .appendQueryParameter("client_secret",CLIENT_SECRET)
                    .appendQueryParameter("v","20180626")
                    .appendQueryParameter("ll",ll)
                    .appendQueryParameter("limit", String.valueOf(8))
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
        return mVenueName;
    }

    public void parseItems( JSONObject jsonBody) throws IOException,JSONException
    {
        JSONObject meta = jsonBody.getJSONObject("meta");
        String status = meta.getString("code");
        JSONObject mResponse = jsonBody.getJSONObject("response");


        if(status.equals("200")) {
            JSONArray groupJsonArray = mResponse.getJSONArray("groups");
            JSONObject temp1 = groupJsonArray.getJSONObject(0);
            JSONArray items = temp1.getJSONArray("items");

            for(int i=0;i<items.length();i++)
            {
               JSONObject singleItem = items.getJSONObject(i);


               JSONObject venue =singleItem.getJSONObject("venue");
               JSONObject location = venue.getJSONObject("location");
               JSONArray category =venue.getJSONArray("categories");

               VenueName temp =new VenueName();
                VenueDetail detail =new VenueDetail();
               detail.setID(venue.getString("id"));
               temp.setName(venue.getString("name"));
               temp.setDistance(location.getInt("distance"));
                detail.setCity(location.getString("city"));
                detail.setState(location.getString("state"));
                detail.setCountry(location.getString("country"));
                detail.setCategory(category.getJSONObject(0).getString("name"));
                temp.setmVenueDetails(detail);
               mVenueName.add(temp);
            }

        }
    }


}
