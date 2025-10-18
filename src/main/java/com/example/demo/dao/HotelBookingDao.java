package com.example.demo.dao;

import com.example.demo.model.BlogPost;
import com.example.demo.model.HotelBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Repository
public class HotelBookingDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int createHotelBooking(Date checkInDate, Date checkOutDate, Integer noOfRooms, String roomType, Integer guestCount,
                                  int cost, Integer numberOfNights,
                                  Integer hotelId, Integer roomId, Integer customerId) {
        String sql = "INSERT INTO Hotel_Booking(check_in_date,check_out_date,no_of_rooms,room_type," +
                "booking_date,guest_count,status,cost,number_of_nights,hotel_id,room_id,customer_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, checkInDate);
            ps.setDate(2, checkOutDate);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ps.setInt(3, noOfRooms); // published_at
            ps.setString(4, roomType);
            ps.setTimestamp(5,now);// last_updated
            ps.setInt(6, guestCount);
            ps.setString(7,"PENDING");
            ps.setInt(8,cost);
            ps.setInt(9,numberOfNights);
            ps.setInt(10,hotelId);
            ps.setInt(11,roomId);
            ps.setInt(12,customerId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public HotelBooking getHotelBookingById(int id) {
        String sql= "SELECT * FROM Hotel_Booking WHERE booking_id=?";
        return jdbcTemplate.queryForObject(sql, new HotelBookingRowMapper(),id);
    }

    public List<HotelBooking> getAllHotelBookingsOfCustomer(Integer userId) {
        String sql="SELECT * FROM Hotel_Booking WHERE customer_id=? AND (status=? OR status=?)";
        return jdbcTemplate.query(sql,new HotelBookingRowMapper(),userId,"CONFIRMED","FINISHED");
    }

    public List<HotelBooking> getAllHotelBookingsOfCustomerByStatus(Integer userId,String status){
        String sql="SELECT * FROM Hotel_Booking WHERE customer_id=? AND status=?";
        return jdbcTemplate.query(sql,new HotelBookingRowMapper(),userId,status);
    }

    public List<HotelBooking> getAllHotelBookingsOfHotel(Integer hotelId){
        String sql="SELECT * FROM Hotel_Booking WHERE hotel_id=?";
        return jdbcTemplate.query(sql,new HotelBookingRowMapper(),hotelId);
    }
    public List<HotelBooking> getAllHotelBookingsOfHotelByStatus(Integer hotelId,String status){
        String sql="SELECT * FROM Hotel_Booking WHERE hotel_id=? AND status = ?";
        return jdbcTemplate.query(sql,new HotelBookingRowMapper(),hotelId,status);
    }

    public int getCountOfBookedRoomsByCheckDates(Date checkIn, Date checkOut, int room_id, int hotel_id) {
        String sql =
                "SELECT COALESCE(SUM(no_of_rooms), 0) " +
                        "FROM Hotel_Booking " +
                        "WHERE hotel_id = ? " +
                        "  AND room_id = ? " +
                        "  AND NOT (check_out_date < ? OR check_in_date > ?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, hotel_id, room_id, checkIn, checkOut);
    }

    public int getAllottedRoomCount(Integer hotelId, Integer roomId) {
        String sql="SELECT total_rooms FROM RoomType WHERE hotel_id=? AND room_id=?  ";
        return jdbcTemplate.queryForObject(sql,Integer.class,hotelId,roomId);
    }

    public void deletePendingBooking() {
        try {
            // Get current date at midnight
            LocalDate today = LocalDate.now();
            Date currentDate = (Date) Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

            String sql = "DELETE FROM Hotel_Booking WHERE status = ? AND booking_date < ?";

            int rowsDeleted = jdbcTemplate.update(sql, "PENDING", currentDate);

            System.out.println("Deleted " + rowsDeleted + " pending bookings older than today");

        } catch (Exception e) {
            System.err.println("Error deleting pending bookings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // RowMapper for HotelBooking
    private static class HotelBookingRowMapper implements RowMapper<HotelBooking> {
        @Override
        public HotelBooking mapRow(ResultSet rs, int rowNum) throws SQLException {
            HotelBooking booking = new HotelBooking();
            booking.setBooking_id(rs.getInt("booking_id"));
            Date checkInSql = rs.getDate("check_in_date");
            booking.setCheck_in_date(checkInSql); // or setCheckInDate(checkInSql) if your model uses java.sql.Date
            Date checkOutSql = rs.getDate("check_out_date");
            booking.setCheck_out_date(checkOutSql); // or setCheckOutDate(checkOutSql)
            booking.setNo_of_rooms(rs.getInt("no_of_rooms"));
            booking.setRoom_type(rs.getString("room_type"));
            booking.setBooking_date(rs.getTimestamp("booking_date"));
            booking.setGuest_count(rs.getInt("guest_count"));
            booking.setStatus(rs.getString("status"));
            int cost = rs.getInt("cost");
            booking.setCost(cost);
            booking.setNumber_of_nights(rs.getInt("number_of_nights"));
            booking.setHotel_id(rs.getInt("hotel_id"));
            booking.setRoom_id(rs.getInt("room_id"));
            booking.setCustomer_id(rs.getInt("customer_id"));
            return booking;
        }
    }
}
