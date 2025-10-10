package com.example.demo.dao;

import com.example.demo.dto.SupportTicketDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.model.SupportTicket;
import com.example.demo.model.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SupportTicketDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer createTicket(SupportTicketDto dto) {
        String sql = "INSERT INTO Support_Ticket (category, status, priority, created_at, ticket_title, ticket_description, booking_id) " +
                "VALUES (?, 'OPEN', 'MEDIUM', ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, dto.getCategory(), Timestamp.valueOf(LocalDateTime.now()), dto.getTicket_title(), dto.getTicket_description(), dto.getBooking_id());
    }

    public Integer addResponse(TicketResponseDto dto, Integer ticketId, Integer staffId, String sender) {
        String sql = "INSERT INTO Ticket_Response (sender, response_text, response_type, is_customer_visible, created_at, ticket_id, staff_id) " +
                "VALUES (?, ?, 'STAFF_REPLY', ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, sender, dto.getResponse_text(), dto.getIs_customer_visible(), Timestamp.valueOf(LocalDateTime.now()), ticketId, staffId);
    }

    public SupportTicket getTicketById(Integer ticketId) {
        String sql = "SELECT * FROM Support_Ticket WHERE ticket_id = ?";
        return jdbcTemplate.queryForObject(sql, new SupportTicketRowMapper(), ticketId);
    }

    public List<TicketResponse> getResponsesForTicket(Integer ticketId) {
        String sql = "SELECT * FROM Ticket_Response WHERE ticket_id = ? ORDER BY created_at ASC";
        return jdbcTemplate.query(sql, new TicketResponseRowMapper(), ticketId);
    }

    public List<SupportTicket> getAllTickets() {
        String sql = "SELECT * FROM Support_Ticket ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new SupportTicketRowMapper());
    }

    public List<SupportTicket> getTicketsByCustomerId(Integer customerId) {
        String sql = "SELECT st.* FROM Support_Ticket st " +
                "JOIN Booking b ON st.booking_id = b.booking_id " +
                "LEFT JOIN Package_Booking pb ON b.package_booking_id = pb.booking_id " +
                "LEFT JOIN Hotel_Booking hb ON b.hotel_booking_id = hb.booking_id " +
                "WHERE pb.customer_id = ? OR hb.customer_id = ?";
        return jdbcTemplate.query(sql, new SupportTicketRowMapper(), customerId, customerId);
    }

    public int updateTicketStatus(Integer ticketId, String status, String priority) {
        if ("RESOLVED".equalsIgnoreCase(status) || "CLOSED".equalsIgnoreCase(status)) {
            String sql = "UPDATE Support_Ticket SET status = ?, priority = ?, resolved_at = ? WHERE ticket_id = ?";
            return jdbcTemplate.update(sql, status, priority, Timestamp.valueOf(LocalDateTime.now()), ticketId);
        } else {
            String sql = "UPDATE Support_Ticket SET status = ?, priority = ?, resolved_at = NULL WHERE ticket_id = ?";
            return jdbcTemplate.update(sql, status, priority, ticketId);
        }
    }

    public int updateSatisfaction(Integer ticketId, String rating) {
        String sql = "UPDATE Support_Ticket SET customer_satisfaction = ? WHERE ticket_id = ?";
        return jdbcTemplate.update(sql, rating, ticketId);
    }

    public boolean doesBookingBelongToCustomer(Integer bookingId, Integer customerId) {
        String sql = "SELECT COUNT(*) FROM Booking b " +
                "LEFT JOIN Package_Booking pb ON b.package_booking_id = pb.booking_id " +
                "LEFT JOIN Hotel_Booking hb ON b.hotel_booking_id = hb.booking_id " +
                "WHERE b.booking_id = ? AND (pb.customer_id = ? OR hb.customer_id = ?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, bookingId, customerId, customerId);
        return count != null && count > 0;
    }

    private static class SupportTicketRowMapper implements RowMapper<SupportTicket> {
        @Override
        public SupportTicket mapRow(ResultSet rs, int rowNum) throws SQLException {
            SupportTicket ticket = new SupportTicket();
            ticket.setTicket_id(rs.getInt("ticket_id"));
            ticket.setCategory(rs.getString("category"));
            ticket.setStatus(rs.getString("status"));
            ticket.setPriority(rs.getString("priority"));
            ticket.setCustomer_satisfaction(rs.getString("customer_satisfaction"));
            ticket.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
            if (rs.getTimestamp("resolved_at") != null) {
                ticket.setResolved_at(rs.getTimestamp("resolved_at").toLocalDateTime());
            }
            ticket.setTicket_title(rs.getString("ticket_title"));
            ticket.setTicket_description(rs.getString("ticket_description"));
            ticket.setBooking_id(rs.getInt("booking_id"));
            return ticket;
        }
    }

    private static class TicketResponseRowMapper implements RowMapper<TicketResponse> {
        @Override
        public TicketResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            TicketResponse response = new TicketResponse();
            response.setResponse_id(rs.getInt("response_id"));
            response.setSender(rs.getString("sender"));
            response.setResponse_text(rs.getString("response_text"));
            response.setResponse_type(rs.getString("response_type"));
            response.setIs_customer_visible(rs.getBoolean("is_customer_visible"));
            response.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
            response.setTicket_id(rs.getInt("ticket_id"));
            response.setStaff_id(rs.getInt("staff_id"));
            return response;
        }
    }
}