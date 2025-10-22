package com.example.demo.dao;

import com.example.demo.model.Booking;
import com.example.demo.model.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.*;

@Repository
public class PaymentDao {

    private JdbcTemplate jdbcTemplate;

    public PaymentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int createPayment(String bookingType, BigDecimal amount, String paymentMode,
                             String status, String transactionReference) {
        String sql = "INSERT INTO Payment (booking_type, payment_datetime, amount, payment_mode, " +
                "status, transaction_reference, refund_amount) VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        //System.out.println("transaction Reference (INSIDE DAO):: "+ transactionReference);

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, bookingType);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setBigDecimal(3, amount);
            ps.setString(4, paymentMode);
            ps.setString(5, status);
            ps.setString(6, transactionReference);
            ps.setBigDecimal(7, BigDecimal.ZERO);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public Payment getPaymentById(Integer paymentId) {
        String sql = "SELECT * FROM Payment WHERE payment_id = ?";
        return jdbcTemplate.queryForObject(sql, new PaymentRowMapper(), paymentId);
    }

    public int createBooking(String bookingType, String bookingStatus,
                             Integer hotelBookingId, Integer paymentId) {
        String sql = "INSERT INTO Booking (booking_type, created_at, booking_status, " +
                "package_booking_id, hotel_booking_id, payment_id) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, bookingType);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setString(3, bookingStatus);
            ps.setNull(4, Types.INTEGER);
            ps.setInt(5, hotelBookingId);
            ps.setInt(6, paymentId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void updateHotelBookingStatus(Integer hotelBookingId, String status) {
        String sql = "UPDATE Hotel_Booking SET status = ? WHERE booking_id = ?";
        jdbcTemplate.update(sql, status, hotelBookingId);
    }

    // Add these methods to PaymentDao.java

    public int createPackageBookingRecord(String bookingType, String bookingStatus,
                                          Integer packageBookingId, Integer paymentId) {
        String sql = "INSERT INTO Booking (booking_type, created_at, booking_status, " +
                "package_booking_id, hotel_booking_id, payment_id) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, bookingType);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.setString(3, bookingStatus);
            ps.setInt(4, packageBookingId);
            ps.setNull(5, Types.INTEGER);
            ps.setInt(6, paymentId);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void updatePackageBookingStatus(Integer packageBookingId, String status) {
        String sql = "UPDATE Package_Booking SET status = ? WHERE booking_id = ?";
        jdbcTemplate.update(sql, status, packageBookingId);
    }

    private static class PaymentRowMapper implements RowMapper<Payment> {
        @Override
        public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Payment payment = new Payment();
            payment.setPayment_id(rs.getInt("payment_id"));
            payment.setBooking_type(rs.getString("booking_type"));
            payment.setPayment_datetime(rs.getTimestamp("payment_datetime"));
            payment.setAmount(rs.getBigDecimal("amount"));
            payment.setPayment_mode(rs.getString("payment_mode"));
            payment.setStatus(rs.getString("status"));
            payment.setTransaction_reference(rs.getString("transaction_reference"));
            payment.setRefund_amount(rs.getBigDecimal("refund_amount"));
            return payment;
        }
    }
}