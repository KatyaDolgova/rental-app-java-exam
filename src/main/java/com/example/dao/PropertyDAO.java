package com.example.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Property;
import com.example.utils.DBConnection;

public class PropertyDAO {

    public void save(Property property) throws SQLException {
        String sql = """
            INSERT INTO properties (owner_id, city, country, description, price_per_night)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, property.getOwnerId());
            stmt.setString(2, property.getCity());
            stmt.setString(3, property.getCountry());
            stmt.setString(4, property.getDescription());
            stmt.setBigDecimal(5, property.getPricePerNight());

            stmt.executeUpdate();
        }
    }
    public Property findById(Long id) throws SQLException {
        String sql = "SELECT * FROM properties WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Property p = new Property();
                p.setId(rs.getLong("id"));
                p.setOwnerId(rs.getLong("owner_id"));
                p.setCity(rs.getString("city"));
                p.setCountry(rs.getString("country"));
                p.setDescription(rs.getString("description"));
                p.setPricePerNight(rs.getBigDecimal("price_per_night"));
                return p;
            }
        }
        return null;
    }
    public List<Property> findPropertiesWithOwnerRating(String city, String country, BigDecimal minPrice, BigDecimal maxPrice) throws SQLException {
        List<Property> properties = new ArrayList<>();
        String sql = """
            SELECT p.*, u.rating AS owner_rating
            FROM properties p
            JOIN users u ON p.owner_id = u.id
            WHERE 1=1
            """;

        StringBuilder where = new StringBuilder();
        if (city != null && !city.trim().isEmpty()) {
            where.append(" AND p.city LIKE ?");
        }
        if (country != null && !country.trim().isEmpty()) {
            where.append(" AND p.country LIKE ?");
        }
        if (minPrice != null) {
            where.append(" AND p.price_per_night >= ?");
        }
        if (maxPrice != null) {
            where.append(" AND p.price_per_night <= ?");
        }

        sql += where;

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            int idx = 1;
            if (city != null && !city.trim().isEmpty()) {
                stmt.setString(idx++, "%" + city + "%");
            }
            if (country != null && !country.trim().isEmpty()) {
                stmt.setString(idx++, "%" + country + "%");
            }
            if (minPrice != null) {
                stmt.setBigDecimal(idx++, minPrice);
            }
            if (maxPrice != null) {
                stmt.setBigDecimal(idx++, maxPrice);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Property p = new Property();
                p.setId(rs.getLong("id"));
                p.setOwnerId(rs.getLong("owner_id"));
                p.setCity(rs.getString("city"));
                p.setCountry(rs.getString("country"));
                p.setDescription(rs.getString("description"));
                p.setPricePerNight(rs.getBigDecimal("price_per_night"));
                p.setOwnerRating(rs.getDouble("owner_rating"));
                properties.add(p);
            }
        }
        return properties;
    }

    public List<Property> findPropertiesByOwnerId(Long ownerId) throws SQLException {
        List<Property> properties = new ArrayList<>();
        String sql = "SELECT * FROM properties WHERE owner_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Property p = new Property();
                p.setId(rs.getLong("id"));
                p.setOwnerId(rs.getLong("owner_id"));
                p.setCity(rs.getString("city"));
                p.setCountry(rs.getString("country"));
                p.setDescription(rs.getString("description"));
                p.setPricePerNight(rs.getBigDecimal("price_per_night"));
                properties.add(p);
            }
        }
        return properties;
    }

    public void update(Property property) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE properties SET ");
        List<Object> params = new ArrayList<>();
        boolean first = true;

        if (property.getCity() != null && !property.getCity().trim().isEmpty()) {
            if (!first) sql.append(", ");
            sql.append("city = ?");
            params.add(property.getCity());
            first = false;
        }
        if (property.getCountry() != null && !property.getCountry().trim().isEmpty()) {
            if (!first) sql.append(", ");
            sql.append("country = ?");
            params.add(property.getCountry());
            first = false;
        }
        if (property.getDescription() != null && !property.getDescription().trim().isEmpty()) {
            if (!first) sql.append(", ");
            sql.append("description = ?");
            params.add(property.getDescription());
            first = false;
        }
        if (property.getPricePerNight() != null) {
            if (!first) sql.append(", ");
            sql.append("price_per_night = ?");
            params.add(property.getPricePerNight());
            first = false;
        }

        if (params.isEmpty()) {
            return; 
        }

        sql.append(" WHERE id = ?");
        params.add(property.getId());

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            stmt.executeUpdate();
        }
    }
    public List<Property> searchProperties(String city, String country,
                                            BigDecimal minPrice, BigDecimal maxPrice) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM properties WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (city != null && !city.trim().isEmpty()) {
            sql.append(" AND city LIKE ?");
            params.add("%" + city + "%");
        }
        if (country != null && !country.trim().isEmpty()) {
            sql.append(" AND country LIKE ?");
            params.add("%" + country + "%");
        }
        if (minPrice != null) {
            sql.append(" AND price_per_night >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND price_per_night <= ?");
            params.add(maxPrice);
        }

        List<Property> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof BigDecimal) {
                    stmt.setBigDecimal(i + 1, (BigDecimal) param);
                } else {
                    stmt.setString(i + 1, param.toString());
                }
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Property p = new Property();
                p.setId(rs.getLong("id"));
                p.setOwnerId(rs.getLong("owner_id"));
                p.setCity(rs.getString("city"));
                p.setCountry(rs.getString("country"));
                p.setDescription(rs.getString("description"));
                p.setPricePerNight(rs.getBigDecimal("price_per_night"));
                list.add(p);
            }
        }
        return list;
    }
}