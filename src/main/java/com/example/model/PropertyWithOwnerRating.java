package com.example.model;

public class PropertyWithOwnerRating {
    private Property property;
    private double ownerRating;

    public PropertyWithOwnerRating(Property property, double ownerRating) {
        this.property = property;
        this.ownerRating = ownerRating;
    }

    public Property getProperty() {
        return property;
    }

    public double getOwnerRating() {
        return ownerRating;
    }
}