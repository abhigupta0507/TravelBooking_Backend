package com.example.demo.service;

import com.example.demo.dao.AuthDao; // Import the new DAO
import com.example.demo.dao.SupportTicketDao;
import com.example.demo.dto.SupportTicketDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.model.SupportTicket;
import com.example.demo.model.TicketResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SupportTicketService {

    @Autowired
    private SupportTicketDao supportTicketDao;

    @Autowired
    private AuthDao authDao; // Inject the new StaffDao

    public Integer createTicket(SupportTicketDto dto, Integer customerId) {
        if (dto.getTicket_title() == null || dto.getTicket_title().isEmpty()) {
            throw new RuntimeException("Ticket title cannot be empty.");
        }
        if (dto.getBooking_id() != null && !supportTicketDao.doesBookingBelongToCustomer(dto.getBooking_id(), customerId)) {
            throw new RuntimeException("Booking ID does not belong to the current user.");
        }
        return supportTicketDao.createTicket(dto);
    }

    // MODIFIED: This method no longer needs staffName as a parameter
    public Integer addResponse(TicketResponseDto dto, Integer ticketId, Integer staffId) {
        if (dto.getResponse_text() == null || dto.getResponse_text().isEmpty()) {
            throw new RuntimeException("Response text cannot be empty.");
        }

        // Use the StaffDao to look up the name from the database
        String staffName = authDao.findStaffNameById(staffId);
        String sender = staffName + " (Staff)";

        return supportTicketDao.addResponse(dto, ticketId, staffId, sender);
    }

    public SupportTicket getTicketById(Integer ticketId) {
        return supportTicketDao.getTicketById(ticketId);
    }

    public List<TicketResponse> getResponsesForTicket(Integer ticketId) {
        return supportTicketDao.getResponsesForTicket(ticketId);
    }

    public List<SupportTicket> getAllTicketsForStaff() {
        return supportTicketDao.getAllTickets();
    }

    public List<SupportTicket> getTicketsForCustomer(Integer customerId) {
        return supportTicketDao.getTicketsByCustomerId(customerId);
    }

    public int updateTicketStatus(Integer ticketId, String status, String priority) {
        return supportTicketDao.updateTicketStatus(ticketId, status, priority);
    }

    public int updateSatisfaction(Integer ticketId, Integer customerId, String rating) {
        SupportTicket ticket = supportTicketDao.getTicketById(ticketId);
        if (!isTicketOwner(ticketId, customerId)) {
            throw new RuntimeException("User is not the owner of this ticket.");
        }
        if (!"RESOLVED".equalsIgnoreCase(ticket.getStatus()) && !"CLOSED".equalsIgnoreCase(ticket.getStatus())) {
            throw new RuntimeException("Cannot rate a ticket that is still open.");
        }
        if (ticket.getCustomer_satisfaction() != null) {
            throw new RuntimeException("Satisfaction rating has already been submitted for this ticket.");
        }
        return supportTicketDao.updateSatisfaction(ticketId, rating);
    }

    public boolean isTicketOwner(Integer ticketId, Integer customerId) {
        SupportTicket ticket = supportTicketDao.getTicketById(ticketId);
        if (ticket.getBooking_id() == null) return false; // Or handle as needed
        return supportTicketDao.doesBookingBelongToCustomer(ticket.getBooking_id(), customerId);
    }
}