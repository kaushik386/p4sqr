package com.p4sqr.poc.p4sqr.Services;

import android.net.Uri;
import android.util.Log;

import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenueDetail;
import com.p4sqr.poc.p4sqr.ExpandableRecycler.VenueName;

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
    private List<VenueName> mVenueNameExplore = new ArrayList<>();
    private List<VenueName> mVenueNameSearch = new ArrayList<>();


    public byte[] getUrlBytes(String urlSpec) throws IOException {
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

        } finally {
            connection.disconnect();
        }
    }


    public String getUrlString(String UrlSpec) throws IOException {
        return new String(getUrlBytes(UrlSpec));
    }

    public List<VenueName> getExploreResult(String ll, String section) {
        try {
            String url = Uri.parse("https://api.foursquare.com/v2/venues/explore")
                    .buildUpon()
                    .appendQueryParameter("client_id", CLIENT_ID)
                    .appendQueryParameter("client_secret", CLIENT_SECRET)
                    .appendQueryParameter("v", "20180626")
                    .appendQueryParameter("ll", ll)
                    .appendQueryParameter("section", section)
                    .appendQueryParameter("limit", String.valueOf(20))
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONObject JsonBody = new JSONObject(jsonString);
            parseItems(JsonBody);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException js) {
            Log.e(TAG, "exception", js);
        }
        return mVenueNameExplore;
    }

    public List<VenueName> getSearchResult(String ll, String query) {
        try {
            String url = Uri.parse("https://api.foursquare.com/v2/venues/search")
                    .buildUpon()
                    .appendQueryParameter("client_id", CLIENT_ID)
                    .appendQueryParameter("client_secret", CLIENT_SECRET)
                    .appendQueryParameter("v", "20180626")
                    .appendQueryParameter("ll", ll)
                    .appendQueryParameter("query", query)
                    .appendQueryParameter("limit", String.valueOf(20))
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONObject JsonBody = new JSONObject(jsonString);
            parseSearchResponse(JsonBody);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException js) {
            Log.e(TAG, "exception", js);
        }
        return mVenueNameSearch;
    }

    public void parseItems(JSONObject jsonBody) throws IOException, JSONException {
        JSONObject meta = jsonBody.getJSONObject("meta");
        String status = meta.getString("code");
        JSONObject mResponse = jsonBody.getJSONObject("response");


        if (status.equals("200")) {
            JSONArray groupJsonArray = mResponse.getJSONArray("groups");
            JSONObject temp1 = groupJsonArray.getJSONObject(0);
            JSONArray items = temp1.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject singleItem = items.getJSONObject(i);


                JSONObject venue = singleItem.getJSONObject("venue");
                JSONObject location = venue.getJSONObject("location");
                JSONArray category = venue.getJSONArray("categories");

                VenueName temp = new VenueName();
                VenueDetail detail = new VenueDetail();
                detail.setID(venue.getString("id"));
                temp.setName(venue.getString("name"));
                if (location.has("distance")) {
                    temp.setDistance(location.getInt("distance"));
                } else {
                    temp.setDistance(-1);

                }
                if (location.has("state")) {
                    detail.setState(location.getString("state"));
                } else {
                    detail.setState("NA");
                }
                if (location.has("city")) {
                    detail.setCity(location.getString("city"));
                } else {
                    detail.setCity("NA");
                }

                if (location.has("country")) {
                    detail.setCountry(location.getString("country"));
                } else {
                    detail.setCountry("NA");
                }

                if (!category.isNull(0)) {
                    detail.setCategory(category.getJSONObject(0).getString("name"));
                } else {
                    detail.setCategory("None");
                }
                temp.setmVenueDetails(detail);
                mVenueNameExplore.add(temp);
            }
        }

    }

    public void parseSearchResponse(JSONObject jsonBody) throws IOException, JSONException {
        JSONObject meta = jsonBody.getJSONObject("meta");
        String status = meta.getString("code");
        JSONObject mResponse = jsonBody.getJSONObject("response");


        if (status.equals("200")) {

            JSONArray venues = mResponse.getJSONArray("venues");

            for (int i = 0; i < venues.length(); i++) {
                JSONObject venue = venues.getJSONObject(i);
                JSONObject location = venue.getJSONObject("location");
                JSONArray category = venue.getJSONArray("categories");
                VenueName temp = new VenueName();
                VenueDetail detail = new VenueDetail();
                detail.setID(venue.getString("id"));
                temp.setName(venue.getString("name"));
                if (location.has("distance")) {
                    temp.setDistance(location.getInt("distance"));
                } else {
                    temp.setDistance(-1);

                }
                if (location.has("state")) {
                    detail.setState(location.getString("state"));
                } else {
                    detail.setState("NA");
                }
                if (location.has("city")) {
                    detail.setCity(location.getString("city"));
                } else {
                    detail.setCity("NA");
                }

                if (location.has("country")) {
                    detail.setCountry(location.getString("country"));
                } else {
                    detail.setCountry("NA");
                }

                if (!category.isNull(0)) {
                    detail.setCategory(category.getJSONObject(0).getString("name"));
                } else {
                    detail.setCategory("None");
                }
                temp.setmVenueDetails(detail);
                mVenueNameSearch.add(temp);
            }

        }
    }


}
