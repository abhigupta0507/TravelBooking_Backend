package com.example.demo.dao;

import com.example.demo.dto.PackageStatus;
import com.example.demo.model.IncludeRooms;
import com.example.demo.model.ItineraryItem;
import com.example.demo.model.TourPackage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public TourPackage findPackageById(Integer packageId){
        String sql = "SELECT * FROM Tour_Package WHERE package_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToPackage(rs), packageId);
        } catch (EmptyResultDataAccessException e) {
            // This is not an error. It's expected if no package matches the slug.
            // Return null to let the service layer know it wasn't found.
            return null;
        }
    }

    /**
     * Inserts a new TourPackage into the database.
     * The slug and created_at fields are handled automatically by the database.
     *
     * @param packageData A TourPackage object containing the data for the new package.
     * @return The auto-generated primary key (package_id) of the newly created package.
     */
    public Integer createPackage(TourPackage packageData) {
        String sql = "INSERT INTO Tour_Package (name, tour_type, image_url, duration_days, price, max_capacity, itinerary_summary, status, avg_rating, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, packageData.getName());
            ps.setString(2, packageData.getTour_type());
            ps.setString(3, packageData.getImage_url());
            ps.setInt(4, packageData.getDuration_days());
            ps.setInt(5, packageData.getPrice());
            ps.setInt(6, packageData.getMax_capacity());
            ps.setString(7, packageData.getItinerary_summary());
            ps.setString(8, packageData.getStatus().name()); // Convert enum to string
            // Handle nullable avg_rating
            if (packageData.getAvg_rating() != null) {
                ps.setFloat(9, packageData.getAvg_rating());
            } else {
                ps.setNull(9, java.sql.Types.DECIMAL);
            }
            ps.setObject(10,LocalDateTime.now());
            return ps;
        }, keyHolder);

        // Safely retrieve the generated key
        Number key = keyHolder.getKey();
        return (key != null) ? key.intValue() : null;
    }


    public void updatePackage(TourPackage packageData) {
        // The slug will be automatically updated by the database trigger if the name changes.
        String sql = "UPDATE Tour_Package SET " +
                "name = ?, tour_type = ?, image_url = ?, duration_days = ?, " +
                "price = ?, max_capacity = ?, itinerary_summary = ?, status = ? " +
                "WHERE package_id = ?";

        jdbcTemplate.update(sql,
                packageData.getName(),
                packageData.getTour_type(),
                packageData.getImage_url(),
                packageData.getDuration_days(),
                packageData.getPrice(),
                packageData.getMax_capacity(),
                packageData.getItinerary_summary(),
                packageData.getStatus().name(),
                packageData.getPackageId());
    }

    public Integer deletePackage(String packageSlug){
        System.out.println(packageSlug);
        String sql = "DELETE FROM Tour_Package WHERE slug = ?";
        return jdbcTemplate.update(sql,packageSlug);
    }


    public List<ItineraryItem> findAllItineraryItemsByPackageId(Integer packageId){
        String sql = "SELECT * FROM Itinerary_Item WHERE package_id = ? ORDER BY day_number, start_time";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToItineraryItem(rs), packageId);
    }

    public ItineraryItem findItineraryItemsById(Integer itemId,Integer packageId){
        String sql = "SELECT * FROM Itinerary_Item WHERE item_id = ? and package_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs,rowNum) -> mapRowToItineraryItem(rs),itemId,packageId);
    }


    /**
     * Inserts a new ItineraryItem into the database.
     * The item_id is auto-incremented and created_at is set to the current time.
     *
     * @param theItem An ItineraryItem object containing the data for the new item.
     * @return The auto-generated primary key (item_id) of the newly created item.
     */
    public Integer createItineraryItem(ItineraryItem theItem) {
        String insertSql = "INSERT INTO Itinerary_Item (package_id, day_number, duration, start_time, end_time, title, description, street_name, city, state, pin, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Step 1: Perform the insert. The database trigger will calculate and set the item_id.
        jdbcTemplate.update(insertSql,
                theItem.getPackage_id(),
                theItem.getDay_number(),
                theItem.getDuration(),
                theItem.getStart_time(),
                theItem.getEnd_time(),
                theItem.getTitle(),
                theItem.getDescription(),
                theItem.getStreet_name(),
                theItem.getCity(),
                theItem.getState(),
                theItem.getPin(),
                LocalDateTime.now()
        );

        // Step 2: Fetch the item we just created by getting the one with the highest item_id for this package.
        String selectSql = "SELECT MAX(item_id) FROM Itinerary_Item WHERE package_id = ?";
        try {
            // Use queryForObject to get a single Integer value.
            return jdbcTemplate.queryForObject(selectSql, Integer.class, theItem.getPackage_id());
        } catch (EmptyResultDataAccessException e) {
            // This should not happen if the insert was successful, but it's good practice to handle it.
            throw new RuntimeException("Failed to retrieve itinerary item ID immediately after creation for package ID: " + theItem.getPackage_id());
        }
    }

    public void insertIncludeRoom(IncludeRooms theRoom) throws Exception{
        String sql = "INSERT INTO Include_Rooms (package_id, hotel_id, room_id, check_in_day, check_out_day) VALUES (?, ?, ?, ?, ?)";
        try{
            jdbcTemplate.update(sql, theRoom.getPackage_id(), theRoom.getHotel_id(), theRoom.getRoom_id(), theRoom.getCheck_in_day(),  theRoom.getCheck_out_day());
        }
        catch (Exception e){
            throw new Exception("Error while Inserting" + e.getMessage());
        }
    }

    public void updateItineraryItem(ItineraryItem theItem){
        String sql = "UPDATE Itinerary_Item SET day_number = ?, duration = ?, start_time = ?, end_time = ?,title = ?, description = ?, street_name = ?, city = ?, state = ?, pin = ?" +
                " WHERE package_id = ? and item_id = ?";

        jdbcTemplate.update(
                sql,
                theItem.getDay_number(),
                theItem.getDuration(),
                theItem.getStart_time(),
                theItem.getEnd_time(),
                theItem.getTitle(),
                theItem.getDescription(),
                theItem.getStreet_name(),
                theItem.getCity(),
                theItem.getState(),
                theItem.getPin(),
                theItem.getPackage_id(),
                theItem.getItem_id());

    }

    public Integer deleteItineraryItem(Integer item_id, Integer package_id) throws SQLException{
        String sql = "DELETE FROM Itinerary_Item WHERE item_id = ? and package_id = ?";
        return jdbcTemplate.update(sql,item_id,package_id);
    }

    public Integer deleteIncludeItem(Integer hotel_id, Integer package_id) throws SQLException{
        String sql = "DELETE FROM Include_Rooms WHERE package_id = ? and hotel_id = ?";
        return jdbcTemplate.update(sql,package_id,hotel_id);
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
