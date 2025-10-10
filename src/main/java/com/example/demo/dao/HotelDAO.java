package com.example.demo.dao;

import com.example.demo.model.Hotel;
import com.example.demo.model.RoomType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class HotelDAO {

    private final JdbcTemplate jdbcTemplate;

    public HotelDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 🔹 1. Fetch all hotels
    public List<Hotel> findAllHotels() {
        String sql = "SELECT * FROM Hotel";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToHotel(rs));
    }

    // 🔹 2. Fetch hotel by ID
    public Hotel findHotelById(Integer hotelId) {
        String sql = "SELECT * FROM Hotel WHERE hotel_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToHotel(rs), hotelId);
        } catch (EmptyResultDataAccessException e) {
            return null; // expected when hotel doesn’t exist
        }
    }

    // 🔹 3. Fetch hotels by city
    public List<Hotel> findHotelsByCity(String city) {
        String sql = "SELECT * FROM Hotel WHERE city = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToHotel(rs), city);
    }

    public List<RoomType> findRoomsByHotelId(Integer hotelId) {
        String sql = "SELECT * FROM RoomType WHERE hotel_id = ?";
        return jdbcTemplate.query(sql, new Object[]{hotelId}, new RoomRowMapper());
    }

    public RoomType findRoomByHotelAndRoomId(int hotelId,int roomId){
        String sql="SELECT * FROM RoomType WHERE hotel_id=? AND room_id=?";
        return jdbcTemplate.queryForObject(sql,new RoomRowMapper(), hotelId,roomId);
    }

    // To update room details
    public boolean updateRoom(RoomType room) {
        String sql = """
        UPDATE RoomType 
        SET balcony_available = ?, 
            cost_per_night = ?, 
            type = ?, 
            bed_type = ?, 
            max_capacity = ?, 
            total_rooms = ?
        WHERE room_id = ? AND hotel_id = ?
    """;

        int rows = jdbcTemplate.update(sql,
                room.getBalcony_available(),
                room.getCost_per_night(),
                room.getType(),
                room.getBed_type(),
                room.getMax_capacity(),
                room.getTotal_rooms(),
                room.getRoom_id(),
                room.getHotel_id());

        return rows > 0;
    }

    public boolean doesRoomBelongToHotel(Integer hotelId, Integer roomId) {
        String sql = "SELECT COUNT(*) FROM RoomType WHERE room_id = ? AND hotel_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, roomId, hotelId);
        return count != null && count > 0;
    }

    // 🔹 4. Insert new hotel
    public Integer createHotel(Hotel hotelData) {
        String sql = "INSERT INTO Hotel (name, street, city, state, pin, rating, total_rooms, vendor_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, hotelData.getName());
            ps.setString(2, hotelData.getStreet());
            ps.setString(3, hotelData.getCity());
            ps.setString(4, hotelData.getState());
            ps.setString(5, hotelData.getPin());
            ps.setBigDecimal(6, hotelData.getRating());
            ps.setInt(7, hotelData.getTotal_rooms());
            ps.setInt(8, hotelData.getVendor_id());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return (key != null) ? key.intValue() : null;
    }

    public void insertRoom(RoomType room) {
        String sql = "INSERT INTO RoomType (hotel_id, room_id, balcony_available, cost_per_night, type, bed_type, max_capacity, total_rooms) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                room.getHotel_id(),
                room.getRoom_id(),
                room.getBalcony_available(),
                room.getCost_per_night(),
                room.getType(),
                room.getBed_type(),
                room.getMax_capacity(),
                room.getTotal_rooms());
    }

    public int updateHotelDetails(Hotel hotel) {
        String sql = "UPDATE Hotel SET " +
                "name = ?, " +
                "street = ?, " +
                "city = ?, " +
                "state = ?, " +
                "pin = ?, " +
                "rating = ?, " +
                "total_rooms = ?, " +
                "vendor_id = ? " +
                "WHERE hotel_id = ?";

        return jdbcTemplate.update(sql,
                hotel.getName(),
                hotel.getStreet(),
                hotel.getCity(),
                hotel.getState(),
                hotel.getPin(),
                hotel.getRating(),
                hotel.getTotal_rooms(),
                hotel.getVendor_id(),
                hotel.getHotel_id());
    }

    // 🔹 5. Delete a hotel
    public boolean deleteHotelById(Integer hotelId) {
        String sql = "DELETE FROM Hotel WHERE hotel_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, hotelId);
        return rowsAffected > 0;
    }

    public Integer getVendorIdByHotelId(Integer hotelId) {
        String sql = "SELECT vendor_id FROM Hotel WHERE hotel_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, hotelId);
    }

    // 🔹 Helper mapper
    private Hotel mapRowToHotel(ResultSet rs) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setHotel_id(rs.getInt("hotel_id"));
        hotel.setName(rs.getString("name"));
        hotel.setStreet(rs.getString("street"));
        hotel.setCity(rs.getString("city"));
        hotel.setState(rs.getString("state"));
        hotel.setPin(rs.getString("pin"));
        hotel.setRating(rs.getBigDecimal("rating"));
        hotel.setTotal_rooms(rs.getInt("total_rooms"));
        hotel.setVendor_id(rs.getInt("vendor_id"));
        return hotel;
    }

    public String getRoomType(Integer hotelId, Integer roomId) {
        String sql="SELECT type FROM RoomType WHERE hotel_id=? AND room_id=?";
        return jdbcTemplate.queryForObject(sql,String.class,hotelId,roomId);
    }

    public int getHotelIdFromVendorId(int vendorId) {
        String sql="SELECT hotel_id FROM Hotel WHERE vendor_id = ?";
        return jdbcTemplate.queryForObject(sql,Integer.class,vendorId);
    }

    private static class RoomRowMapper implements RowMapper<RoomType> {
        @Override
        public RoomType mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new RoomType(
                    rs.getInt("hotel_id"),
                    rs.getInt("room_id"),
                    rs.getBoolean("balcony_available"),
                    rs.getInt("cost_per_night"),
                    rs.getString("type"),
                    rs.getString("bed_type"),
                    rs.getInt("max_capacity"),
                    rs.getInt("total_rooms")
            );
        }
    }
}
