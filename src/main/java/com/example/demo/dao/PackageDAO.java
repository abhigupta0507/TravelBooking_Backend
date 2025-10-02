package com.example.demo.dao;

import com.example.demo.dto.PackageStatus;
import com.example.demo.model.ItineraryItem;
import com.example.demo.model.TourPackage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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

    public TourPackage findPackageBySlug(String slug){
        String sql = "SELECT * FROM Tour_Package WHERE slug = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToPackage(rs), slug);
        } catch (EmptyResultDataAccessException e) {
            // This is not an error. It's expected if no package matches the slug.
            // Return null to let the service layer know it wasn't found.
            return null;
        }
    }

    public List<ItineraryItem> findAllItineraryItemsByPackageId(Integer packageId){
        String sql = "SELECT * FROM Itinerary_Item WHERE package_id = ? ORDER BY day_number, start_time";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToItineraryItem(rs), packageId);
    }

    private ItineraryItem mapRowToItineraryItem(ResultSet rs) throws SQLException {
        ItineraryItem item = new ItineraryItem();
        item.setPackage_id(rs.getInt("package_id"));
        item.setItem_id(rs.getInt("item_id"));
        item.setDay_number(rs.getInt("day_number"));
        item.setDuration(rs.getInt("duration"));
        item.setStart_time(rs.getObject("start_time", LocalDateTime.class));
        item.setEnd_time(rs.getObject("end_time", LocalDateTime.class));
        item.setTitle(rs.getString("title"));
        item.setDescription(rs.getString("description"));
        item.setStreet_name(rs.getString("street_name"));
        item.setCity(rs.getString("city"));
        item.setState(rs.getString("state"));
        item.setPin(rs.getString("pin"));
        item.setCreated_at(rs.getObject("created_at", LocalDateTime.class));
        return item;
    }

    private TourPackage mapRowToPackage(ResultSet rs) throws SQLException {
        TourPackage thePackage = new TourPackage();
        thePackage.setPackageId(rs.getInt("package_id"));
        thePackage.setTour_type(rs.getString("tour_type"));
        thePackage.setName(rs.getString("name"));
        thePackage.setSlug(rs.getString("slug"));
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
