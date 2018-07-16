package com.p4sqr.poc.p4sqr.Model;

import java.util.List;

public class Venue {

    String id;
    String name;
    VenueLocation location;
    List<Category> categories;
    String prefix;
    String suffix;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }





    public VenueLocation getLocation() {
        return location;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getId() {

        return id;
    }

    public String getName() {
        return name;
    }

}
