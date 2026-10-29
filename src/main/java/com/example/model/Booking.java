package com.example.model;

import java.time.LocalDate;

public class Booking {
    private Long id;
    private Long propertyId;
    private Long renterId; // тот, кто арендует
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean completed = false;
    private Integer ratingGivenByRenter = null; // рейтинг от арендатора
    private Integer ratingGivenByOwner = null;  // рейтинг от владельца
    private String status = "ACTIVE";
    private LocalDate newStartDate;
    private LocalDate newEndDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public Long getRenterId() { return renterId; }
    public void setRenterId(Long renterId) { this.renterId = renterId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Integer getRatingGivenByRenter() { return ratingGivenByRenter; }
    public void setRatingGivenByRenter(Integer ratingGivenByRenter) { this.ratingGivenByRenter = ratingGivenByRenter; }

    public Integer getRatingGivenByOwner() { return ratingGivenByOwner; }
    public void setRatingGivenByOwner(Integer ratingGivenByOwner) { this.ratingGivenByOwner = ratingGivenByOwner; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getNewStartDate() { return newStartDate; }
    public void setNewStartDate(LocalDate newStartDate) { this.newStartDate = newStartDate; }

    public LocalDate getNewEndDate() { return newEndDate; }
    public void setNewEndDate(LocalDate newEndDate) { this.newEndDate = newEndDate; }
}