package com.example.demo.controller;

import com.example.demo.dto.SupportTicketDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.model.SupportTicket;
import com.example.demo.model.TicketResponse;
import com.example.demo.service.SupportTicketService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/support")
public class SupportTicketController {

    @Autowired
    private SupportTicketService supportTicketService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/tickets")
    public ResponseEntity<?> createTicket(@RequestHeader("Authorization") String authHeader, @RequestBody SupportTicketDto dto) {
        try {
            String token = authHeader.substring(7);
            if (!"CUSTOMER".equalsIgnoreCase(jwtUtil.getUserTypeFromToken(token))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only customers can create tickets.");
            }
            Integer customerId = jwtUtil.getUserIdFromToken(token);
            supportTicketService.createTicket(dto, customerId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Support ticket created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ... (no changes to getAllTickets, getCustomerTickets, getTicketDetails)

    @GetMapping("/tickets")
    public ResponseEntity<?> getAllTickets(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            if (!"STAFF".equalsIgnoreCase(jwtUtil.getUserTypeFromToken(token))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
            }
            List<SupportTicket> tickets = supportTicketService.getAllTicketsForStaff();
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/tickets/my-tickets")
    public ResponseEntity<?> getCustomerTickets(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            if (!"CUSTOMER".equalsIgnoreCase(jwtUtil.getUserTypeFromToken(token))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
            }
            Integer customerId = jwtUtil.getUserIdFromToken(token);
            List<SupportTicket> tickets = supportTicketService.getTicketsForCustomer(customerId);
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<?> getTicketDetails(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Integer ticketId) {
        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            if ("CUSTOMER".equalsIgnoreCase(userType)) {
                if (!supportTicketService.isTicketOwner(ticketId, userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to view this ticket.");
                }
            } else if (!"STAFF".equalsIgnoreCase(userType)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
            }

            SupportTicket ticket = supportTicketService.getTicketById(ticketId);
            List<TicketResponse> responses = supportTicketService.getResponsesForTicket(ticketId);
            return ResponseEntity.ok(Map.of("ticket", ticket, "responses", responses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/tickets/{id}/responses")
    public ResponseEntity<?> addResponse(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Integer ticketId, @RequestBody TicketResponseDto dto) {
        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            Integer userId = jwtUtil.getUserIdFromToken(token);

            if ("STAFF".equalsIgnoreCase(userType)) {
                // Existing logic for staff responses
                supportTicketService.addResponse(dto, ticketId, userId);
                return ResponseEntity.ok("Staff response added successfully.");

            } else if ("CUSTOMER".equalsIgnoreCase(userType)) {
                // New logic for customer responses
                supportTicketService.addCustomerResponse(dto.getResponse_text(), ticketId, userId);
                return ResponseEntity.ok("Your response has been added successfully.");

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to respond to tickets.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ... (no changes to updateTicketStatus or updateSatisfaction)

    @PutMapping("/tickets/{id}/status")
    public ResponseEntity<?> updateTicketStatus(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Integer ticketId, @RequestBody Map<String, String> statusUpdate) {
        try {
            String token = authHeader.substring(7);
            if (!"STAFF".equalsIgnoreCase(jwtUtil.getUserTypeFromToken(token))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only staff can update tickets.");
            }
            String status = statusUpdate.get("status");
            String priority = statusUpdate.get("priority");
            supportTicketService.updateTicketStatus(ticketId, status, priority);
            return ResponseEntity.ok("Ticket updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/tickets/{id}/satisfaction")
    public ResponseEntity<?> updateSatisfaction(@RequestHeader("Authorization") String authHeader, @PathVariable("id") Integer ticketId, @RequestBody Map<String, String> payload) {
        try {
            String token = authHeader.substring(7);
            if (!"CUSTOMER".equalsIgnoreCase(jwtUtil.getUserTypeFromToken(token))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only customers can provide feedback.");
            }
            Integer customerId = jwtUtil.getUserIdFromToken(token);
            String rating = payload.get("satisfaction");

            if (rating == null || rating.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Satisfaction rating is required.");
            }

            supportTicketService.updateSatisfaction(ticketId, customerId, rating);
            return ResponseEntity.ok("Thank you for your feedback.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}