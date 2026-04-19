package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(
        name = "inventory_vehicle",
        indexes = @Index(name = "idx_inv_dealer_place", columnList = "dealer_place_id")
)
public class InventoryVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dealer_place_id", nullable = false, length = 512)
    private String dealerPlaceId;

    //private Integer year;
    @Column(name = "vehicle_year")
    private Integer year;
    private String make;
    private String model;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    private Integer mileage;

    @Column(length = 16)
    private String conditionType;

    @Column(length = 2048)
    private String imageUrl;

    @Column(length = 32)
    private String vin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealerPlaceId() {
        return dealerPlaceId;
    }

    public void setDealerPlaceId(String dealerPlaceId) {
        this.dealerPlaceId = dealerPlaceId;
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

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}
