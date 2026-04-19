package com.example.demo.model;

public class Dealer{
    /** Google Place ID — exposed as JSON "id" for the React app */
    private String id;
    private String name;
    private Double rating;
    private String address;
    private double lat;
    private double lng;
    private String image;
    private String mapUrl;
    /** From Google Place Details */
    private String phone;
    private String website;
    /** How many vehicles this dealer has (for UI before full /vehicles fetch) */
    private Integer vehicleCount;

    public Dealer(String name, Double rating, String address, double lat, double lng){
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }
    public Double getRating() { return rating; }
    public String getAddress() { return address; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }

    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }

    public String getMapUrl(){
        return mapUrl;
    }
    public void setMapUrl(String mapUrl){
        this.mapUrl = mapUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(Integer vehicleCount) {
        this.vehicleCount = vehicleCount;
    }
}