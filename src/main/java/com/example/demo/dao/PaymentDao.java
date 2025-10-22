package com.example.demo.dao;

import com.example.demo.model.Booking;
import com.example.demo.model.Payment;
import com.example.demo.model.Refund;
import com.example.demo.model.User;
import com.mysql.cj.result.Row;
import org.springframework.cglib.core.Local;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

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

    public Booking getBookingByPaymentId(Integer paymentId){
        String sql = "SELECT * FROM Booking WHERE payment_id = ?";
        return jdbcTemplate.queryForObject(sql,new BookingRowMapper(),paymentId);
    }


    /**
     * Creates a new refund request in the database.
     * It manually calculates the next sequential refund_id for the given payment_id.
     *
     * @param theRefund A Refund object containing the initial data (payment_id, amount, reason).
     * @return The newly generated refund_id for this transaction.
     */
    public Integer createRefundRequest(Refund theRefund) {
        // Step 1: Calculate the next refund_id for this specific payment.
        // It will be 1 for the first refund, 2 for the second, and so on.
        String idQuery = "SELECT IFNULL(MAX(refund_id), 0) + 1 FROM Refund WHERE payment_id = ?";
        Integer newRefundId = jdbcTemplate.queryForObject(idQuery, Integer.class, theRefund.getPayment_id());

        // Step 2: Prepare the INSERT statement.
        String sql = "INSERT INTO Refund(payment_id, refund_id, refund_amount, processing_charges, refund_reason, processed_at, refund_status) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        // Step 3: Execute the update.
        jdbcTemplate.update(sql,
                theRefund.getPayment_id(),
                newRefundId, // Use the new ID we just calculated
                theRefund.getRefund_amount(),
                theRefund.getProcessing_charges(),
                theRefund.getRefund_reason(),
                LocalDateTime.now(),
                theRefund.getRefund_status() // Convert enum to string
        );

        // Step 4: Return the new ID.
        return newRefundId;
    }

    /**
     * Checks if a refund with a final status (COMPLETED or FAILED) already exists
     * for a given payment ID.
     * @param paymentId The ID of the payment to check.
     * @return true if such a refund exists, false otherwise.
     */
    public boolean hasFinalizedRefund(Integer paymentId) {
        String sql = "SELECT COUNT(*) FROM Refund WHERE payment_id = ? AND refund_status IN ('COMPLETED', 'PROCESSING')";

        // queryForObject is used to get a single value back.
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, paymentId);

        // If the count is not null and greater than 0, a refund exists.
        return count != null && count > 0;
    }

    /**
     * Fetches all refund records from the database.
     * @return A list of all Refund objects.
     */
    public List<Refund> findAllRefunds() {
        String sql = "SELECT * FROM Refund ORDER BY processed_at DESC";
        return jdbcTemplate.query(sql, new RefundRowMapper());
    }

    /**
     * Fetches all refund records that match a specific status.
     * @param status The RefundStatus enum to filter by.
     * @return A list of matching Refund objects.
     */
    public List<Refund> findAllByStatus(String status) {
        String sql = "SELECT * FROM Refund WHERE refund_status = ? ORDER BY processed_at DESC";
        return jdbcTemplate.query(sql, new RefundRowMapper(), status);
    }

    /**
     * Finds a specific refund by its composite primary key.
     * @param paymentId The ID of the parent payment.
     * @param refundId The ID of the refund.
     * @return A Refund object, or null if not found.
     */
    public Refund findRefundById(Integer paymentId, Integer refundId) {
        // Assuming you have a RefundRowMapper similar to what we created before
        String sql = "SELECT * FROM Refund WHERE payment_id = ? AND refund_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new RefundRowMapper(), paymentId, refundId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * Updates the status and reference of an existing refund record.
     * @param refund The Refund object with the updated data.
     * @return The number of rows affected (should be 1 on success).
     */
    public int updateRefund(Refund refund) {
        String sql = "UPDATE Refund SET refund_status = ?, reference = ?, processed_at = ? " +
                "WHERE payment_id = ? AND refund_id = ?";

        return jdbcTemplate.update(sql,
                refund.getRefund_status(),
                refund.getReference(),
                LocalDateTime.now(), // Update the processed_at timestamp
                refund.getPayment_id(),
                refund.getRefund_id()
        );
    }

    /**
     * A RowMapper to map a row from the Booking table to a Booking model object.
     */
    public class BookingRowMapper implements RowMapper<Booking> {

        @Override
        public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
            Booking booking = new Booking();

            booking.setBooking_id(rs.getInt("booking_id"));
            booking.setBooking_type(rs.getString("booking_type"));
            booking.setCreated_at(rs.getTimestamp("created_at"));
            booking.setBooking_status(rs.getString("booking_status"));

            // Use getObject for nullable integer columns to correctly handle NULLs.
            // rs.getInt() would return 0 for a NULL value, which is incorrect.
            booking.setPackage_booking_id(rs.getObject("package_booking_id", Integer.class));
            booking.setHotel_booking_id(rs.getObject("hotel_booking_id", Integer.class));
            booking.setPayment_id(rs.getObject("payment_id", Integer.class));

            return booking;
        }
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


    /**
     * A private helper class to map a row from the Refund table to a Refund object.
     */
    private static class RefundRowMapper implements RowMapper<Refund> {
        @Override
        public Refund mapRow(ResultSet rs, int rowNum) throws SQLException {
            Refund refund = new Refund();
            refund.setPayment_id(rs.getInt("payment_id"));
            refund.setRefund_id(rs.getInt("refund_id"));
            refund.setRefund_amount(rs.getBigDecimal("refund_amount"));
            refund.setProcessing_charges(rs.getBigDecimal("processing_charges"));
            refund.setRefund_reason(rs.getString("refund_reason"));
            refund.setProcessed_at(rs.getObject("processed_at", LocalDateTime.class));
            refund.setReference(rs.getString("reference"));

            String statusStr = rs.getString("refund_status");
            if (statusStr != null) {
                try {
                    refund.setRefund_status((statusStr.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    // Handle cases where the status in the DB is invalid
                    refund.setRefund_status(null);
                }
            }
            return refund;
        }
    }
}