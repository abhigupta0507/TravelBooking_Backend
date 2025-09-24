package com.example.demo.dao;

import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public class AuthDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean emailExists(String email) {
        String customerQuery = "SELECT COUNT(*) FROM Customer WHERE email = ?";
        String vendorQuery = "SELECT COUNT(*) FROM Vendor WHERE email = ?";
        String staffQuery = "SELECT COUNT(*) FROM Staff WHERE email = ?";

        int customerCount = jdbcTemplate.queryForObject(customerQuery, Integer.class, email);
        int vendorCount = jdbcTemplate.queryForObject(vendorQuery, Integer.class, email);
        int staffCount = jdbcTemplate.queryForObject(staffQuery, Integer.class, email);

        return customerCount > 0 || vendorCount > 0 || staffCount > 0;
    }

    public User findUserByEmailAndType(String email, String userType) {
        try {
            switch (userType.toUpperCase()) {
                case "CUSTOMER":
                    return findCustomerByEmail(email);
                case "VENDOR":
                    return findVendorByEmail(email);
                case "STAFF":
                    return findStaffByEmail(email);
                default:
                    return null;
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User findCustomerByEmail(String email) {
        String sql = "SELECT customer_id, first_name, last_name, email, phone FROM Customer WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper("CUSTOMER"), email);
    }

    private User findVendorByEmail(String email) {
        String sql = "SELECT vendor_id, contact_person_first_name, contact_person_last_name, email, phone FROM Vendor WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new VendorRowMapper(), email);
    }

    public User findStaffByEmail(String email) {
        String sql = "SELECT staff_id, first_name, last_name, email, phone, role FROM Staff WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new StaffRowMapper(), email);
    }

    public Integer createCustomer(String firstName, String lastName, String email, String phone,
                                  String dateOfBirth, String gender, String emergencyContactFirstName,
                                  String emergencyContactLastName, String emergencyContactNo) {
        String sql = "INSERT INTO Customer (first_name, last_name, phone, email, date_of_birth, gender, " +
                "emergency_contact_first_name, emergency_contact_last_name, emergency_contact_no, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setDate(5, dateOfBirth != null ? java.sql.Date.valueOf(dateOfBirth) : null);
            ps.setString(6, gender);
            ps.setString(7, emergencyContactFirstName);
            ps.setString(8, emergencyContactLastName);
            ps.setString(9, emergencyContactNo);
            ps.setObject(10, LocalDateTime.now());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public Integer createVendor(String vendorName, String serviceType, String contactPersonFirstName,
                                String contactPersonLastName, String email, String phone) {
        String sql = "INSERT INTO Vendor (vendor_name, service_type, contact_person_first_name, " +
                "contact_person_last_name, email, phone, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, vendorName != null ? vendorName : (contactPersonFirstName + " " + contactPersonLastName + " Services"));
            ps.setString(2, serviceType);
            ps.setString(3, contactPersonFirstName);
            ps.setString(4, contactPersonLastName);
            ps.setString(5, email);
            ps.setString(6, phone);
            ps.setString(7, "ACTIVE");
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public Integer createStaff(String firstName, String lastName, String email, String phone, String role) {
        String sql = "INSERT INTO Staff (employee_code, first_name, last_name, email, phone, " +
                "joining_date, role, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "EMP" + System.currentTimeMillis()); // Generate employee code
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setDate(6, java.sql.Date.valueOf(LocalDate.now()));
            ps.setString(7, role);
            ps.setBigDecimal(8, new java.math.BigDecimal("25000.00")); // Default salary
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    private static class UserRowMapper implements RowMapper<User> {
        private String userType;

        public UserRowMapper(String userType) {
            this.userType = userType;
        }

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt(1));
            user.setFirstName(rs.getString(2));
            user.setLastName(rs.getString(3));
            user.setEmail(rs.getString(4));
            user.setPhone(rs.getString(5));
            user.setUserType(userType);
            return user;
        }
    }

    private static class VendorRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt(1));
            user.setFirstName(rs.getString(2));
            user.setLastName(rs.getString(3));
            user.setEmail(rs.getString(4));
            user.setPhone(rs.getString(5));
            user.setUserType("VENDOR");
            return user;
        }
    }

    private static class StaffRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt(1));
            user.setFirstName(rs.getString(2));
            user.setLastName(rs.getString(3));
            user.setEmail(rs.getString(4));
            user.setPhone(rs.getString(5));
            user.setRole(rs.getString(6));
            user.setUserType("STAFF");
            return user;
        }
    }
}