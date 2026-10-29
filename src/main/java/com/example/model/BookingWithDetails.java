package com.example.model;

public class BookingWithDetails {
    private Booking booking;
    private Property property;
    private String renterNickname;

    public BookingWithDetails(Booking booking, Property property, String renterNickname) {
        this.booking = booking;
        this.property = property;
        this.renterNickname = renterNickname;
    }

    public Booking getBooking() { return booking; }
    public Property getProperty() { return property; }
    public String getRenterNickname() { return renterNickname; }
}
