package com.example.nearestlocation.data;

import java.util.List;

public class PlacesResponse {
    public List<Result> results;

    public static class Result {
        public String name;
        public Geometry geometry;
    }

    public static class Geometry {
        public Location location;
    }

    public static class Location {
        public double lat;
        public double lng;
    }
}
