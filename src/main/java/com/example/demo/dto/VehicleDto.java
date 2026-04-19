package com.example.demo.dto;

import java.math.BigDecimal;

public class VehicleDto {

    private Long id;
    private Integer year;
    private String make;
    private String model;
    private BigDecimal price;
    private Integer mileage;
    private String condition;
    private String image;
    private String vin;

    public VehicleDto() {
    }

    public VehicleDto(Long id, Integer year, String make, String model,
                      BigDecimal price, Integer mileage, String condition, String image, String vin) {
        this.id = id;
        this.year = year;
        this.make = make;
        this.model = model;
        this.price = price;
        this.mileage = mileage;
        this.condition = condition;
        this.image = image;
        this.vin = vin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}
