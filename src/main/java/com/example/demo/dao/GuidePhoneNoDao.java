package com.example.demo.dao;

import com.example.demo.model.GuidePhoneNo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GuidePhoneNoDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int addGuidePhoneNo(Integer guideId, String phoneNo) {
        String sql = "INSERT INTO Guide_PhoneNo (guide_id, phone_no) VALUES (?, ?)";
        try {
            return jdbcTemplate.update(sql, guideId, phoneNo);
        } catch (DuplicateKeyException e) {
            // PK constraint (guide_id, phone_no) prevents duplicates
            throw new RuntimeException("This phone number already exists for this guide.");
        }
    }

    // ✅ 2. Get all phone numbers for a guide
    public List<GuidePhoneNo> getPhoneNosByGuide(Integer guideId) {
        String sql = "SELECT * FROM Guide_PhoneNo WHERE guide_id = ?";
        return jdbcTemplate.query(sql, new GuidePhoneNoRowMapper(), guideId);
    }

    // ✅ 3. Delete a specific phone number of a guide
    public int deleteGuidePhoneNo(Integer guideId, String phoneNo) {
        String sql = "DELETE FROM Guide_PhoneNo WHERE guide_id = ? AND phone_no = ?";
        return jdbcTemplate.update(sql, guideId, phoneNo);
    }

    // ✅ 4. Delete all phone numbers of a guide (optional, for manual cleanup)
    public int deleteAllPhoneNosOfGuide(Integer guideId) {
        String sql = "DELETE FROM Guide_PhoneNo WHERE guide_id = ?";
        return jdbcTemplate.update(sql, guideId);
    }

    // ✅ RowMapper for GuidePhoneNo
    private static class GuidePhoneNoRowMapper implements RowMapper<GuidePhoneNo> {
        @Override
        public GuidePhoneNo mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GuidePhoneNo(
                    rs.getInt("guide_id"),
                    rs.getString("phone_no")
            );
        }
    }
}
