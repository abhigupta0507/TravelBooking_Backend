package com.example.demo.dao;

import com.example.demo.dto.PackageOverview;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PackageDAO {
    private JdbcTemplate jdbcTemplate;

    public PackageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PackageOverview> findAllPackages(){
        String sql = "SELECT * FROM Tour_Package";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToPackage(rs));
    }

    private PackageOverview mapRowToPackage(ResultSet rs) throws SQLException {
        PackageOverview thePackage = new PackageOverview();
        thePackage.setPackageId(rs.getInt("package_id"));
        thePackage.setTour_type(rs.getString("tour_type"));
        thePackage.setName(rs.getString("name"));
        thePackage.setImage_url(rs.getString("image_url"));
        thePackage.setDuration_days(rs.getInt("duration_days"));
        thePackage.setMax_capacity(rs.getInt("max_capacity"));
        thePackage.setItinerary_summary(rs.getString("itinerary_summary"));
        thePackage.setStatus(rs.getString("status"));
        thePackage.setAvg_rating(rs.getFloat("avg_rating")); // Ensure this line is present and correct
//        doctor.setPost(rs.getString("Post"));
//        doctor.setDepartment(rs.getString("Department"));
//        doctor.setSpecialization(rs.getString("Specialization"));
//        doctor.setPassword(rs.getString("Password")); // Ensure this line is present and correct
        return thePackage;
    }
}
