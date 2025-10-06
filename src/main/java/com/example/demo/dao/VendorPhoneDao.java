package com.example.demo.dao;

import com.example.demo.model.GuidePhoneNo;
import com.example.demo.model.VendorPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class VendorPhoneDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int addVendorPhoneNo(Integer vendorId, String phoneNo) {
        String sql = "INSERT INTO Vendor_Phone (vendor_id, phone) VALUES (?, ?)";
        try {
            return jdbcTemplate.update(sql, vendorId, phoneNo);
        } catch (DuplicateKeyException e) {
            // PK constraint (vendor_id, phone_no) prevents duplicates
            throw new RuntimeException("This phone number already exists for this vendor.");
        }
    }

    // ✅ 2. Get all phone numbers for a guide
    public List<VendorPhone> getPhoneNosByVendor(Integer vendorId) {
        String sql = "SELECT * FROM Vendor_Phone WHERE vendor_id = ?";
        return jdbcTemplate.query(sql, new VendorPhoneRowMapper(), vendorId);
    }

    // ✅ 3. Delete a specific phone number of a vendor
    public int deleteVendorPhoneNo(Integer vendorId, String phoneNo) {
        String sql = "DELETE FROM Vendor_Phone WHERE vendor_id = ? AND phone = ?";
        return jdbcTemplate.update(sql, vendorId, phoneNo);
    }

    // ✅ 4. Delete all phone numbers of a vendor (optional, for manual cleanup)
    public int deleteAllPhoneNosOfVendor(Integer vendorId) {
        String sql = "DELETE FROM Vendor_Phone WHERE vendor_id = ?";
        return jdbcTemplate.update(sql, vendorId);
    }

    // ✅ RowMapper for VendorPhone
    private static class VendorPhoneRowMapper implements RowMapper<VendorPhone> {
        @Override
        public VendorPhone mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new VendorPhone(
                    rs.getInt("vendor_id"),
                    rs.getString("phone")
            );
        }
    }
}
