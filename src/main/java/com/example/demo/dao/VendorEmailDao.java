package com.example.demo.dao;

import com.example.demo.model.GuideEmail;
import com.example.demo.model.VendorEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VendorEmailDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int addVendorEmail(Integer vendorId, String email) {
        String sql = "INSERT INTO Vendor_Email (vendor_id, email) VALUES (?, ?)";
        try {
            return jdbcTemplate.update(sql, vendorId, email);
        } catch (DuplicateKeyException e) {
            // PK constraint (vendor_id, email) prevents duplicates
            throw new RuntimeException("This email already exists for this vendor.");
        }
    }

    // ✅ 2. Get all emails for a vendor
    public List<VendorEmail> getEmailsByVendor(Integer vendorId) {
        String sql = "SELECT * FROM Vendor_Email WHERE vendor_id = ?";
        return jdbcTemplate.query(sql, new VendorEmailRowMapper(), vendorId);
    }

    // ✅ 3. Delete a specific email of a vendor
    public int deleteVendorEmail(Integer vendorId, String email) {
        String sql = "DELETE FROM Vendor_Email WHERE vendor_id = ? AND email = ?";
        return jdbcTemplate.update(sql, vendorId, email);
    }

    // ✅ RowMapper for VendorEmail
    private static class VendorEmailRowMapper implements RowMapper<VendorEmail> {
        @Override
        public VendorEmail mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new VendorEmail(
                    rs.getInt("vendor_id"),
                    rs.getString("email")
            );
        }
    }
}
