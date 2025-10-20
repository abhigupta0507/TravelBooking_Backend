package com.example.demo.dao;

import com.example.demo.model.PackageReview;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PackageReviewDAO {

    private final JdbcTemplate jdbcTemplate;

    public PackageReviewDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private PackageReview mapRowToReview(ResultSet rs, int rowNum) throws SQLException {
        PackageReview review = new PackageReview();
        review.setReview_id(rs.getInt("review_id"));
        review.setOverall_rating(rs.getBigDecimal("overall_rating"));
        review.setTitle(rs.getString("title"));
        review.setBody(rs.getString("body"));
        review.setCould_recommend(rs.getBoolean("could_recommend"));
        review.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
        review.setPackage_booking_id(rs.getInt("package_booking_id"));
        return review;
    }

    // ✅ CREATE
    public int addReview(PackageReview review) {
        String sql = """
            INSERT INTO Package_Review (overall_rating, title, body, could_recommend, created_at, package_booking_id)
            VALUES (?, ?, ?, ?, NOW(), ?)
        """;
        return jdbcTemplate.update(sql,
                review.getOverall_rating(),
                review.getTitle(),
                review.getBody(),
                review.getCould_recommend(),
                review.getPackage_booking_id());
    }

    // ✅ GET by Package ID
    public List<PackageReview> getReviewsByPackageId(Integer packageId) {
        String sql = """
            SELECT r.* FROM Package_Review r
            JOIN Package_Booking b ON r.package_booking_id = b.booking_id
            WHERE b.package_id = ?
        """;
        return jdbcTemplate.query(sql, this::mapRowToReview, packageId);
    }

    // ✅ GET by User ID
    public List<PackageReview> getReviewsByUserId(Integer userId) {
        String sql = """
            SELECT r.* FROM Package_Review r
            JOIN Package_Booking b ON r.package_booking_id = b.booking_id
            WHERE b.customer_id = ?
        """;
        return jdbcTemplate.query(sql, this::mapRowToReview, userId);
    }

    // ✅ DELETE by Review ID
    public int deleteReview(Integer reviewId) {
        String sql = "DELETE FROM Package_Review WHERE review_id = ?";
        return jdbcTemplate.update(sql, reviewId);
    }

    public Integer getUserIdByPackageBookingId(Integer packageBookingId) {
        String sql = "SELECT customer_id FROM Package_Booking WHERE booking_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, packageBookingId);
        } catch (Exception e) {
            System.out.println("Error fetching userId for package_booking_id " + packageBookingId + ": " + e.getMessage());
            return null;
        }
    }

    public String getPackageBookingStatus(int packageBookingId) {
        String sql = "SELECT booking_status FROM Booking WHERE booking_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, packageBookingId);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            // No matching booking found
            return null;
        }
    }

    public int getCustomerIdByReviewId(Integer reviewId) {
        String sql = "SELECT b.customer_id from Package_Booking b JOIN Package_Review r ON r.package_booking_id = b.booking_id WHERE r.review_id = ? ";
        return jdbcTemplate.queryForObject(sql, Integer.class, reviewId);
    }

    public Boolean doesReviewExist(Integer packageBookingId){
        String sql = "SELECT EXISTS (SELECT 1 from Package_Review WHERE package_booking_id = ?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, packageBookingId) > 0;
    }
}
