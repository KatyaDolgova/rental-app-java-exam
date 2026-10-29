package com.example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.model.User;
import com.example.utils.DBConnection;

public class UserDAO {

    public void save(User user) throws SQLException {
        String sql = """
        INSERT INTO users (full_name, city, country, nickname, password, birth_date, email, rating_sum, rating_count)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getCity());
            stmt.setString(3, user.getCountry());
            stmt.setString(4, user.getNickname());
            stmt.setString(5, user.getPassword());
            stmt.setDate(6, Date.valueOf(user.getBirthDate()));
            stmt.setString(7, user.getEmail());
            stmt.setDouble(8, user.getRatingSum());
            stmt.setInt(9, user.getRatingCount());

            stmt.executeUpdate();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062 || e.getSQLState().equals("23000")) {
                throw new IllegalArgumentException("Email or nickname already exists");
            }
            throw e;
        }
    }

    public User findByNickname(String nickname) throws SQLException {
        String sql = "SELECT * FROM users WHERE nickname = ?";
        return findUserByField(sql, nickname);
    }

    public User findByUserId(Long userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        return findUserByField(sql, userId);
    }
    
    private User findUserByField(String sql, Object param) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (param instanceof String) {
                stmt.setString(1, (String) param);
            } else {
                stmt.setLong(1, (Long) param);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setFullName(rs.getString("full_name"));
                user.setCity(rs.getString("city"));
                user.setCountry(rs.getString("country"));
                user.setNickname(rs.getString("nickname"));
                user.setPassword(rs.getString("password"));
                user.setBirthDate(rs.getDate("birth_date").toLocalDate());
                user.setEmail(rs.getString("email"));
                user.setRating(rs.getDouble("rating"));
                user.setBlocked(rs.getBoolean("is_blocked"));
                user.setRatingSum(rs.getDouble("rating_sum"));
                user.setRatingCount(rs.getInt("rating_count"));
                return user;
            }
        }
        return null;
    }

    public void blockUser(Long userId) throws SQLException {
        String sql = "UPDATE users SET is_blocked = true WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.executeUpdate();
        }
    }

    public void update(User user) throws SQLException {
        String sql = """
            UPDATE users
            SET full_name = ?, city = ?, country = ?, email = ?, password = ?
            WHERE id = ?
            """;
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getCity());
            stmt.setString(3, user.getCountry());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPassword());
            stmt.setLong(6, user.getId());

            stmt.executeUpdate();
        }
    }
}