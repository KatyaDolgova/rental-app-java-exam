package com.example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Booking;
import com.example.model.BookingWithDetails;
import com.example.model.Property;
import com.example.utils.DBConnection;

public class BookingDAO {

    public void save(Booking booking) throws SQLException {
        String sql = """
            INSERT INTO bookings (property_id, renter_id, start_date, end_date, completed)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, booking.getPropertyId());
            stmt.setLong(2, booking.getRenterId());
            stmt.setDate(3, Date.valueOf(booking.getStartDate()));
            stmt.setDate(4, Date.valueOf(booking.getEndDate()));
            stmt.setBoolean(5, booking.isCompleted());

            stmt.executeUpdate();
        }
    }

    public void markAsCompleted(Long bookingId) throws SQLException {
        String sql = "UPDATE bookings SET completed = true WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, bookingId);
            stmt.executeUpdate();
        }
    }

    public Booking findById(Long id) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapBooking(rs);
            }
        }
        return null;
    }

    public List<Booking> findBookingsByRenterId(Long renterId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE renter_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, renterId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(mapBooking(rs));
            }
        }
        return bookings;
    }

    public List<BookingWithDetails> findBookingsForOwner(Long ownerId) throws SQLException {
        List<BookingWithDetails> result = new ArrayList<>();
        String sql = """
            SELECT b.*, p.id AS p_id, p.owner_id, p.city, p.country, p.description, p.price_per_night,
                   u.nickname AS renter_nickname
            FROM bookings b
            JOIN properties p ON b.property_id = p.id
            JOIN users u ON b.renter_id = u.id
            WHERE p.owner_id = ?
            ORDER BY b.id DESC
            """;
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Booking b = mapBooking(rs);

                Property p = new Property();
                p.setId(rs.getLong("p_id"));
                p.setOwnerId(rs.getLong("owner_id"));
                p.setCity(rs.getString("city"));
                p.setCountry(rs.getString("country"));
                p.setDescription(rs.getString("description"));
                p.setPricePerNight(rs.getBigDecimal("price_per_night"));

                result.add(new BookingWithDetails(b, p, rs.getString("renter_nickname")));
            }
        }
        return result;
    }

    public void markExpiredBookingsAsCompleted() throws SQLException {
        String sql = "UPDATE bookings SET completed = true WHERE end_date < CURDATE() AND completed = false";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            int updatedRows = stmt.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("✅ Marked " + updatedRows + " expired bookings as completed.");
            }
        }
    }

    public void updateStatus(Long bookingId, String status) throws SQLException {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setLong(2, bookingId);
            stmt.executeUpdate();
        }
    }

    public void saveModificationRequest(Long bookingId, LocalDate newStartDate, LocalDate newEndDate) throws SQLException {
        String sql = "UPDATE bookings SET status = 'MODIFY_REQUESTED', new_start_date = ?, new_end_date = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(newStartDate));
            stmt.setDate(2, Date.valueOf(newEndDate));
            stmt.setLong(3, bookingId);
            stmt.executeUpdate();
        }
    }

    public void applyModification(Long bookingId) throws SQLException {
        String sql = """
            UPDATE bookings
            SET start_date = new_start_date, end_date = new_end_date,
                new_start_date = NULL, new_end_date = NULL, status = 'ACTIVE'
            WHERE id = ?
            """;
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, bookingId);
            stmt.executeUpdate();
        }
    }

    public void revertToActive(Long bookingId) throws SQLException {
        String sql = "UPDATE bookings SET status = 'ACTIVE', new_start_date = NULL, new_end_date = NULL WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, bookingId);
            stmt.executeUpdate();
        }
    }

    public void cancelBooking(Long bookingId) throws SQLException {
        String sql = "UPDATE bookings SET status = 'CANCELLED' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, bookingId);
            stmt.executeUpdate();
        }
    }

    private Booking mapBooking(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId(rs.getLong("id"));
        b.setPropertyId(rs.getLong("property_id"));
        b.setRenterId(rs.getLong("renter_id"));
        b.setStartDate(rs.getDate("start_date").toLocalDate());
        b.setEndDate(rs.getDate("end_date").toLocalDate());
        b.setCompleted(rs.getBoolean("completed"));
        b.setRatingGivenByRenter(rs.getObject("rating_given_by_renter", Integer.class));
        b.setRatingGivenByOwner(rs.getObject("rating_given_by_owner", Integer.class));
        b.setStatus(rs.getString("status"));
        Date newStart = rs.getDate("new_start_date");
        Date newEnd = rs.getDate("new_end_date");
        b.setNewStartDate(newStart != null ? newStart.toLocalDate() : null);
        b.setNewEndDate(newEnd != null ? newEnd.toLocalDate() : null);
        return b;
    }
}
