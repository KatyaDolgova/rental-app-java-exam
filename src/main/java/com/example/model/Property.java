package com.example.model;

import java.math.BigDecimal;

public class Property {
    private Long id;
    private Long ownerId;
    private String city;
    private String country;
    private String description;
    private BigDecimal pricePerNight;
    private Double ownerRating;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(BigDecimal pricePerNight) { this.pricePerNight = pricePerNight; }

    public Double getOwnerRating() { return ownerRating; }
    public void setOwnerRating(Double ownerRating) { this.ownerRating = ownerRating; }
}