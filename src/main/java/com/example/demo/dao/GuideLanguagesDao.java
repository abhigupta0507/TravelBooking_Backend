package com.example.demo.dao;

import com.example.demo.model.GuideLanguages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GuideLanguagesDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Add a language for a guide
    public int addGuideLanguage(Integer guideId, String language) {
        String sql = "INSERT INTO Guide_Languages (guide_id, language) VALUES (?, ?)";
        try {
            return jdbcTemplate.update(sql, guideId, language);
        } catch (DuplicateKeyException e) {
            // PK constraint (guide_id, language) prevents duplicates
            throw new RuntimeException("This language already exists for this guide.");
        }
    }

    // Get all languages for a guide
    public List<GuideLanguages> getLanguagesByGuide(Integer guideId) {
        String sql = "SELECT * FROM Guide_Languages WHERE guide_id = ?";
        return jdbcTemplate.query(sql, new GuideLanguageRowMapper(), guideId);
    }

    // Delete a specific language of a guide
    public int deleteGuideLanguage(Integer guideId, String language) {
        String sql = "DELETE FROM Guide_Languages WHERE guide_id = ? AND language = ?";
        return jdbcTemplate.update(sql, guideId, language);
    }

    // RowMapper for GuideLanguage
    private static class GuideLanguageRowMapper implements RowMapper<GuideLanguages> {
        @Override
        public GuideLanguages mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new GuideLanguages(
                    rs.getInt("guide_id"),
                    rs.getString("language")
            );
        }
    }
}
