package com.example.demo.dao;

import com.example.demo.model.HotelEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HotelEmailDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int addHotelEmail(Integer hotelId, String email) {
        String sql = "INSERT INTO HotelEmail (hotel_id, email) VALUES (?, ?)";
        try {
            return jdbcTemplate.update(sql, hotelId, email);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("This email already exists for this hotel.");
        }
    }

    public List<HotelEmail> getEmailsByHotel(Integer hotelId) {
        String sql = "SELECT * FROM HotelEmail WHERE hotel_id = ?";
        return jdbcTemplate.query(sql, new HotelEmailRowMapper(), hotelId);
    }

    public int deleteHotelEmail(Integer hotelId, String email) {
        String sql = "DELETE FROM HotelEmail WHERE hotel_id = ? AND email = ?";
        return jdbcTemplate.update(sql, hotelId, email);
    }

    private static class HotelEmailRowMapper implements RowMapper<HotelEmail> {
        @Override
        public HotelEmail mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new HotelEmail(
                    rs.getInt("hotel_id"),
                    rs.getString("email")
            );
        }
    }

    public Integer getVendorIdByHotelId(Integer hotelId) {
        String sql = "SELECT vendor_id FROM Hotel WHERE hotel_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, hotelId);
    }
}
