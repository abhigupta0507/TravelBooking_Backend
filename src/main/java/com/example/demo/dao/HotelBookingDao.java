package com.example.demo.dao;

import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public Integer updateHotelBookingStatus(String status,Integer booking_id) {
        String sql= "UPDATE Hotel_Booking SET status=? WHERE booking_id=?";
        return jdbcTemplate.update(sql  ,status,booking_id);
    }

    public Integer updateBookingStatus(String status,Integer booking_id) {
        String sql= "UPDATE Booking SET booking_status=? WHERE booking_id=?";
        return jdbcTemplate.update(sql  ,status,booking_id);
    }

    public List<HotelBooking> getAllHotelBookingsOfCustomer(Integer userId) {
        String sql="SELECT * FROM Hotel_Booking WHERE customer_id=? AND (status=? OR status=?)";
        return jdbcTemplate.query(sql,new HotelBookingRowMapper(),userId,"CONFIRMED","FINISHED");
    }

    public List<HotelBooking> getAllHotelBookingsOfCustomerByStatus(Integer userId,String status){
        String sql="SELECT * FROM Hotel_Booking WHERE customer_id=? AND status=?";
        System.out.println(status);
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

    public Booking getBookingForHotelBooking(Integer hotelBookingId){
        String sql = "SELECT * FROM Booking WHERE hotel_booking_id=?";
        List<Booking> results = jdbcTemplate.query(sql, new BookingRowMapper(), hotelBookingId);
        Booking result = results.isEmpty() ? null : results.get(0);
        System.out.println(result);
        return result;
//         System.out.println("In the DAO and about to fetch booking for hotel_booking_id: "+hotelBookingId);
//         return jdbcTemplate.queryForObject(sql,new BookingRowMapper(),hotelBookingId);
    }

    public void deletePendingBooking() {
        try {
            // Get current date at midnight
            LocalDate today = LocalDate.now();
            java.util.Date currentDate =  Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

            String sql = "DELETE FROM Hotel_Booking WHERE status = ? AND booking_date < ?";

            int rowsDeleted = jdbcTemplate.update(sql, "PENDING", currentDate);

            System.out.println("Deleted " + rowsDeleted + " pending bookings older than today");

        } catch (Exception e) {
            System.err.println("Error deleting pending bookings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add this method to fetch payment for a booking

    public Payment getPaymentForBooking(int bookingId) {
        String sql = "SELECT p.* FROM payment p " +
                "JOIN booking b ON p.payment_id = b.payment_id " +
                "WHERE b.hotel_booking_id = ?";

        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Payment.class), bookingId);
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

    /**
     * A RowMapper to map a row from the Booking table to a Booking model object.
     */
    private static class BookingRowMapper implements RowMapper<Booking> {
        @Override
        public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
            Booking booking = new Booking();

            booking.setBooking_id(rs.getInt("booking_id"));
            booking.setBooking_type(rs.getString("booking_type"));
            booking.setCreated_at(rs.getTimestamp("created_at"));
            booking.setBooking_status(rs.getString("booking_status"));

            // Use getObject for nullable integer columns to correctly handle NULLs.
            booking.setPackage_booking_id(rs.getObject("package_booking_id", Integer.class));
            booking.setHotel_booking_id(rs.getObject("hotel_booking_id", Integer.class));
            booking.setPayment_id(rs.getObject("payment_id", Integer.class));

            return booking;
        }
    }


    private static class HotelAssignmentRowMapper implements RowMapper<HotelAssignment> {
        @Override
        public HotelAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
            HotelAssignment booking = new HotelAssignment();
            //LocalDateTime createdAt= rs.getTimestamp("created_at");
            booking.setCreated_at(rs.getTimestamp("created_at"));
            booking.setItem_id(rs.getInt("item_id"));
            booking.setHotel_booking_id(rs.getInt("hotel_booking_id"));
            booking.setPackage_id(rs.getInt("package_id"));
            booking.setPackage_booking_id(rs.getInt("package_booking_id"));
            return booking;
        }
    }
}
