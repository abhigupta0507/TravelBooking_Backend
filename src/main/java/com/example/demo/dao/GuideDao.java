package com.example.demo.dao;

import com.example.demo.model.Guide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class GuideDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ✅ 1. Get all guides (all vendors)
    public List<Guide> getAllGuides() {
        String sql = "SELECT * FROM Guide";
        return jdbcTemplate.query(sql, new GuideRowMapper());
    }

    // ✅ 2. Get all guides of a vendor
    public List<Guide> getGuidesByVendor(Integer vendorId) {
        String sql = "SELECT * FROM Guide WHERE vendor_id = ?";
        return jdbcTemplate.query(sql, new GuideRowMapper(), vendorId);
    }

    // ✅ 3. Get a particular guide of a vendor
    public Guide getGuideByVendorAndId(Integer vendorId, Integer guideId) {
        String sql = "SELECT * FROM Guide WHERE vendor_id = ? AND guide_id = ?";
        return jdbcTemplate.queryForObject(sql, new GuideRowMapper(), vendorId, guideId);
    }

    // ✅ 4. Create a new guide (check vendor outside this DAO in service layer)
    public Integer createGuide(Guide guide, Integer vendorId) {
        String sql = "INSERT INTO Guide (first_name, last_name, experience, cost_per_hour, profile_photo, availability, created_at, vendor_id, primary_email, primary_phone, primary_language) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, guide.getFirst_name());
            ps.setString(2, guide.getLast_name());
            ps.setObject(3, guide.getExperience(), Types.INTEGER);
            ps.setBigDecimal(4, guide.getCost_per_hour());
            ps.setString(5, guide.getProfile_photo());
            ps.setBoolean(6, guide.getAvailability() != null ? guide.getAvailability() : true);
            ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(8, vendorId);
            ps.setString(9, guide.getPrimary_email());
            ps.setString(10, guide.getPrimary_phone());
            ps.setString(11, guide.getPrimary_language());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    // ✅ 5. Update a guide of a vendor
    public int updateGuide(Integer vendorId, Integer guideId, Guide guide) {
        String sql = "UPDATE Guide SET first_name=?, last_name=?, experience=?, cost_per_hour=?, profile_photo=?, availability=?, primary_email=?, primary_phone=?, primary_language=? " +
                "WHERE vendor_id=? AND guide_id=?";

        return jdbcTemplate.update(sql,
                guide.getFirst_name(),
                guide.getLast_name(),
                guide.getExperience(),
                guide.getCost_per_hour(),
                guide.getProfile_photo(),
                guide.getAvailability(),
                guide.getPrimary_email(),
                guide.getPrimary_phone(),
                guide.getPrimary_language(),
                vendorId,
                guideId
        );
    }

    // ✅ 6. Delete a guide of a vendor
    public int deleteGuide(Integer vendorId, Integer guideId) {
        String sql = "DELETE FROM Guide WHERE vendor_id=? AND guide_id=?";
        return jdbcTemplate.update(sql, vendorId, guideId);
    }

    // ✅ RowMapper for Guide
    private static class GuideRowMapper implements RowMapper<Guide> {
        @Override
        public Guide mapRow(ResultSet rs, int rowNum) throws SQLException {
            Guide guide = new Guide();
            guide.setGuide_id(rs.getInt("guide_id"));
            guide.setFirst_name(rs.getString("first_name"));
            guide.setLast_name(rs.getString("last_name"));
            guide.setExperience(rs.getObject("experience", Integer.class));
            guide.setCost_per_hour(rs.getBigDecimal("cost_per_hour"));
            guide.setProfile_photo(rs.getString("profile_photo"));
            guide.setAvailability(rs.getBoolean("availability"));
            guide.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
            guide.setVendor_id(rs.getInt("vendor_id"));
            guide.setPrimary_email(rs.getString("primary_email"));
            guide.setPrimary_phone(rs.getString("primary_phone"));
            guide.setPrimary_language(rs.getString("primary_language"));
            return guide;
        }
    }
}
