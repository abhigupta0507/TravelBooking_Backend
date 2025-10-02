package com.example.demo.dao;

import com.example.demo.model.GuideEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GuideEmailDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public int addGuideEmail(Integer guideId, String email) {
        String sql = "INSERT INTO Guide_Email (guide_id, email) VALUES (?, ?)";
        try {
            return jdbcTemplate.update(sql, guideId, email);
        } catch (DuplicateKeyException e) {
            // PK constraint (guide_id, email) prevents duplicates
            throw new RuntimeException("This email already exists for this guide.");
        }
    }

    // ✅ 2. Get all emails for a guide
    public List<GuideEmail> getEmailsByGuide(Integer guideId) {
        String sql = "SELECT * FROM Guide_Email WHERE guide_id = ?";
        return jdbcTemplate.query(sql, new GuideEmailRowMapper(), guideId);
    }

    // ✅ 3. Delete a specific email of a guide
    public int deleteGuideEmail(Integer guideId, String email) {
        String sql = "DELETE FROM Guide_Email WHERE guide_id = ? AND email = ?";
        return jdbcTemplate.update(sql, guideId, email);
    }

    // ✅ RowMapper for GuideEmail
    private static class GuideEmailRowMapper implements RowMapper<GuideEmail> {
        @Override
        public GuideEmail mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GuideEmail(
                    rs.getInt("guide_id"),
                    rs.getString("email")
            );
        }
    }
}
