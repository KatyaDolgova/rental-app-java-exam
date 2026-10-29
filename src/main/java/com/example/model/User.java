package com.example.model;

import java.time.LocalDate;

public class User {
    private Long id;
    private String fullName;
    private String city;
    private String country;
    private String nickname;
    private String password;
    private LocalDate birthDate;
    private String email;
    private double rating = 5.0;
    private boolean isBlocked = false;

    private double ratingSum = 0.0;
    private int ratingCount = 0;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }

    // Новые геттеры/сеттеры
    public double getRatingSum() { return ratingSum; }
    public void setRatingSum(double ratingSum) { this.ratingSum = ratingSum; }

    public int getRatingCount() { return ratingCount; }
    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }
}