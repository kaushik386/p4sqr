package com.p4sqr.poc.p4sqr.Model;

public class AllData {

    Predictions predictions;
    Result result;

    public void setPredictions(Predictions predictions) {
        this.predictions = predictions;
    }

    public Predictions getPredictions() {
        return predictions;
    }

    public Result getResult() {
        return result;
    }
}

class Result
{
Geometry geometry;

    public Geometry getGeometry() {
        return geometry;
    }
}

class Geometry
{
    Location location;

    public Location getLocation() {
        return location;
    }
}

class Location
{
    String lat;

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    String lng;
}