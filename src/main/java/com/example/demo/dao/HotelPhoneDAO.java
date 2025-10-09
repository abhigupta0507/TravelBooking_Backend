package com.example.demo.dao;

import com.example.demo.model.HotelPhone;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class HotelPhoneDAO {

    private final JdbcTemplate jdbcTemplate;

    public HotelPhoneDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 🔹 GET: Fetch all phone numbers for a given hotel ID
    public List<HotelPhone> getPhoneNumbersByHotelId(Integer hotelId) {
        String sql = "SELECT * FROM HotelPhone WHERE hotel_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToHotelPhone(rs), hotelId);
    }

    // 🔹 POST: Add multiple phone numbers for a hotel
    public void addPhoneNumbers(Integer hotelId, List<String> phoneNumbers) {
        String sql = "INSERT INTO HotelPhone (hotel_id, phone) VALUES (?, ?)";
        for (String phone : phoneNumbers) {
            jdbcTemplate.update(sql, hotelId, phone);
        }
    }

    // 🔹 PUT: Update a specific phone number
    public int updatePhoneNumber(Integer hotelId, String oldPhone, String newPhone) {
        String sql = "UPDATE HotelPhone SET phone = ? WHERE hotel_id = ? AND phone = ?";
        return jdbcTemplate.update(sql, newPhone, hotelId, oldPhone);
    }

    // 🔹 PUT (remove): Delete a specific phone number
    public int deletePhoneNumber(Integer hotelId, String phone) {
        String sql = "DELETE FROM HotelPhone WHERE hotel_id = ? AND phone = ?";
        return jdbcTemplate.update(sql, hotelId, phone);
    }

    // Helper method
    private HotelPhone mapRowToHotelPhone(ResultSet rs) throws SQLException {
        HotelPhone hp = new HotelPhone();
        hp.setHotel_id(rs.getInt("hotel_id"));
        hp.setPhone(rs.getString("phone"));
        return hp;
    }

    public Integer getVendorIdByHotelId(Integer hotelId) {
        String sql = "SELECT vendor_id FROM Hotel WHERE hotel_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, hotelId);
    }
}
