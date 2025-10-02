package com.example.demo.dao;

import com.example.demo.model.DriverPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DriverPhoneDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ✅ 1. Add a new driver phone
    public int addDriverPhone(Integer driverId, String phone) {
        String sql = "INSERT INTO Driver_Phone (driver_id, phone) VALUES (?, ?)";
        try {
            return jdbcTemplate.update(sql, driverId, phone);
        } catch (DuplicateKeyException e) {
            // Prevent duplicate phone for same driver
            throw new RuntimeException("This phone number already exists for this driver.");
        }
    }

    // ✅ 2. Get all phone numbers for a driver
    public List<DriverPhone> getPhonesByDriver(Integer driverId) {
        String sql = "SELECT * FROM Driver_Phone WHERE driver_id = ?";
        return jdbcTemplate.query(sql, new DriverPhoneRowMapper(), driverId);
    }

    // ✅ 3. Delete a specific phone number of a driver
    public int deleteDriverPhone(Integer driverId, String phone) {
        String sql = "DELETE FROM Driver_Phone WHERE driver_id = ? AND phone = ?";
        return jdbcTemplate.update(sql, driverId, phone);
    }

    // ✅ RowMapper for DriverPhone
    private static class DriverPhoneRowMapper implements RowMapper<DriverPhone> {
        @Override
        public DriverPhone mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new DriverPhone(
                    rs.getInt("driver_id"),
                    rs.getString("phone")
            );
        }
    }
}
