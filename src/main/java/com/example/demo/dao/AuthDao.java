package com.example.demo.dao;

import com.example.demo.dto.SignupRequest;
import com.example.demo.model.Customer;
import com.example.demo.model.Staff;
import com.example.demo.model.User;
import com.example.demo.model.Vendor;
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
                    Customer theCustomer = findCustomerByEmail(email);
                    theCustomer.setUserType("CUSTOMER");
                    return theCustomer;
                case "VENDOR":
                    Vendor theVendor = findVendorByEmail(email);
                    theVendor.setUserType("VENDOR");
                    return theVendor;
                case "STAFF":
                    Staff theStaff = findStaffByEmail(email);
                    theStaff.setUserType("STAFF");
                    return theStaff;
                default:
                    return null;
            }
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Customer findCustomerByEmail(String email) {
        String sql = "SELECT customer_id, first_name, last_name, phone, email, password, " +
                "date_of_birth, gender, emergency_contact_first_name, emergency_contact_last_name, " +
                "emergency_contact_no, created_at FROM Customer WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), email);
    }

    public Vendor findVendorByEmail(String email) {
        String sql = "SELECT vendor_id, vendor_name, service_type, contact_person_first_name, " +
                "contact_person_last_name, email, password, phone, street_name, city, state, pin, " +
                "amt_due, account_no, ifsc_code, status FROM Vendor WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new VendorRowMapper(), email);
    }

    public Staff findStaffByEmail(String email) {
        String sql = "SELECT staff_id, employee_code, first_name, last_name, email, password, phone, " +
                "joining_date, role, salary FROM Staff WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new StaffRowMapper(), email);
    }

    public Integer createCustomer(String firstName, String lastName, String email, String password, String phone,
                                  String dateOfBirth, String gender, String emergencyContactFirstName,
                                  String emergencyContactLastName, String emergencyContactNo, String notImp) {
        String sql = "INSERT INTO Customer (first_name, last_name, phone, email, password, date_of_birth, gender, " +
                "emergency_contact_first_name, emergency_contact_last_name, emergency_contact_no, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, phone);
            ps.setString(4, email);
            ps.setString(5, password);
            if (dateOfBirth != null && !dateOfBirth.isEmpty()) {
                ps.setDate(6, java.sql.Date.valueOf(dateOfBirth));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }
            ps.setString(7, gender);
            ps.setString(8, emergencyContactFirstName);
            ps.setString(9, emergencyContactLastName);
            ps.setString(10, emergencyContactNo);
            ps.setObject(11, LocalDateTime.now());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public Integer createStaff(String firstName, String lastName, String email, String password, String phone, String role) {
        String sql = "INSERT INTO Staff (employee_code, first_name, last_name, email, password, phone, " +
                "joining_date, role, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, "EMP" + System.currentTimeMillis());
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, email);
            ps.setString(5, password);
            ps.setString(6, phone);
            ps.setDate(7, java.sql.Date.valueOf(LocalDate.now()));
            ps.setString(8, role);
            ps.setBigDecimal(9, new java.math.BigDecimal("25000.00"));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public boolean checkRequiredFieldsPresent(SignupRequest request) {
        return (request.getEmail() != null) && (request.getFirstName() != null) &&
                (request.getPhone() != null);
    }

    public Integer createVendor(
            String vendorName,
            String serviceType,
            String contactPersonFirstName,
            String contactPersonLastName,
            String email,
            String password,
            String phone,
            String city,
            String amountDue,
            String accountNo,
            String ifscCode,
            String pin,
            String state,
            String streetName) {

        String sql = "INSERT INTO Vendor (" +
                "vendor_name, service_type, contact_person_first_name, contact_person_last_name, " +
                "email, password, phone, street_name, city, state, pin, amt_due, account_no, ifsc_code, status" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

//        System.out.println("Creating vendor with name: " + vendorName);
//        System.out.println("Service type: " + serviceType);
//        System.out.println("Contact person: " + contactPersonFirstName + " " + contactPersonLastName);
//        System.out.println("Amount due: " + amountDue);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Handle vendor_name - if null, create a default name
            String finalVendorName = vendorName;
            if (finalVendorName == null || finalVendorName.trim().isEmpty()) {
                finalVendorName = contactPersonFirstName + " " + contactPersonLastName + " Services";
            }
            ps.setString(1, finalVendorName);

            ps.setString(2, serviceType != null ? serviceType : "General");
            ps.setString(3, contactPersonFirstName != null ? contactPersonFirstName : "Unknown");
            ps.setString(4, contactPersonLastName != null ? contactPersonLastName : "Unknown");
            ps.setString(5, email);
            ps.setString(6, password);
            ps.setString(7, phone);
            ps.setString(8, streetName);
            ps.setString(9, city);
            ps.setString(10, state);
            ps.setString(11, pin);

            // Convert amountDue to BigDecimal if not null
            if (amountDue != null && !amountDue.trim().isEmpty()) {
                try {
                    ps.setBigDecimal(12, new java.math.BigDecimal(amountDue));
                } catch (NumberFormatException e) {
                    ps.setBigDecimal(12, java.math.BigDecimal.ZERO);
                }
            } else {
                ps.setBigDecimal(12, java.math.BigDecimal.ZERO);
            }

            ps.setString(13, accountNo);
            ps.setString(14, ifscCode);
            ps.setString(15, "ACTIVE");
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    private static class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            // SQL: customer_id, first_name, last_name, phone, email, password,
            // date_of_birth, gender, emergency_contact_first_name, emergency_contact_last_name,
            // emergency_contact_no, created_at
            Customer user = new Customer();
            user.setCustomer_id(rs.getInt("customer_id"));
            user.setFirst_name(rs.getString("first_name"));
            user.setLast_name(rs.getString("last_name"));
            user.setPhone(rs.getString("phone"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));

            // Handle date_of_birth
            java.sql.Date dobSql = rs.getDate("date_of_birth");
            if (dobSql != null) {
                user.setDate_of_birth(dobSql.toLocalDate());
            }

            user.setGender(rs.getString("gender"));
            user.setEmergency_contact_first_name(rs.getString("emergency_contact_first_name"));
            user.setEmergency_contact_last_name(rs.getString("emergency_contact_last_name"));
            user.setEmergency_contact_no(rs.getString("emergency_contact_no"));

            // Handle created_at
            java.sql.Timestamp createdAtSql = rs.getTimestamp("created_at");
            if (createdAtSql != null) {
                user.setCreated_at(createdAtSql.toLocalDateTime());
            }

            return user;
        }
    }

    private static class VendorRowMapper implements RowMapper<Vendor> {
        @Override
        public Vendor mapRow(ResultSet rs, int rowNum) throws SQLException {
            // SQL: vendor_id, vendor_name, service_type, contact_person_first_name, contact_person_last_name,
            // email, password, phone, street_name, city, state, pin, amt_due, account_no, ifsc_code, status
            Vendor user = new Vendor();
            user.setVendor_id(rs.getInt("vendor_id"));
            user.setVendor_name(rs.getString("vendor_name"));
            user.setService_type(rs.getString("service_type"));
            user.setContact_person_first_name(rs.getString("contact_person_first_name"));
            user.setContact_person_last_name(rs.getString("contact_person_last_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setPhone(rs.getString("phone"));
            user.setStreet_name(rs.getString("street_name"));
            user.setCity(rs.getString("city"));
            user.setState(rs.getString("state"));
            user.setPin(rs.getString("pin"));
            user.setAmt_due(rs.getBigDecimal("amt_due"));
            user.setAccount_no(rs.getString("account_no"));
            user.setIfsc_code(rs.getString("ifsc_code"));
            user.setStatus(rs.getString("status"));
            return user;
        }
    }

    private static class StaffRowMapper implements RowMapper<Staff> {
        @Override
        public Staff mapRow(ResultSet rs, int rowNum) throws SQLException {
            // SQL: staff_id, employee_code, first_name, last_name, email, password, phone, joining_date, role, salary
            Staff user = new Staff();
            user.setStaff_id(rs.getInt("staff_id"));
            user.setEmployee_code(rs.getString("employee_code"));
            user.setFirst_name(rs.getString("first_name"));
            user.setLast_name(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setPhone(rs.getString("phone"));
            user.setJoining_date(rs.getDate("joining_date"));
            user.setRole(rs.getString("role"));
            user.setSalary(rs.getBigDecimal("salary"));
            return user;
        }
    }
}