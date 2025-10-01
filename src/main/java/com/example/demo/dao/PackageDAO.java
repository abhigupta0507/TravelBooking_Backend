package com.example.demo.dao;

import com.example.demo.dto.PackageStatus;
import com.example.demo.model.TourPackage;
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

    public List<TourPackage> findAllPackages(){
        String sql = "SELECT * FROM Tour_Package";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToPackage(rs));
    }

    public List<TourPackage> findAllPackagesByStatus(PackageStatus status){
        String sql = "SELECT * FROM Tour_Package WHERE status = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToPackage(rs), status.name());
    }

    private TourPackage mapRowToPackage(ResultSet rs) throws SQLException {
        TourPackage thePackage = new TourPackage();
        thePackage.setPackageId(rs.getInt("package_id"));
        thePackage.setTour_type(rs.getString("tour_type"));
        thePackage.setName(rs.getString("name"));
        thePackage.setImage_url(rs.getString("image_url"));
        thePackage.setDuration_days(rs.getInt("duration_days"));
        thePackage.setPrice(rs.getInt("price"));
        thePackage.setMax_capacity(rs.getInt("max_capacity"));
        thePackage.setItinerary_summary(rs.getString("itinerary_summary"));
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            thePackage.setStatus(PackageStatus.valueOf(statusStr.toUpperCase()));
        }
        thePackage.setAvg_rating(rs.getFloat("avg_rating"));
        thePackage.setCreated_at(rs.getObject("created_at", java.time.LocalDateTime.class));

        return thePackage;
    }
}
