package com.example.demo.dao;

import com.example.demo.dto.SignupRequest;
import com.example.demo.dto.StaffDTO;
import com.example.demo.dto.VendorDTO;
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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    public Customer findCustomerById(int profileId) {
        String sql= "SELECT * FROM Customer WHERE customer_id=?";
        return jdbcTemplate.queryForObject(sql,new CustomerRowMapper(),profileId);
    }

    public Vendor findVendorById(int profileId) {
        String sql= "SELECT * FROM Vendor WHERE vendor_id=?";
        return jdbcTemplate.queryForObject(sql,new VendorRowMapper(),profileId);
    }

    public Staff findStaffById(int profileId) {
        String sql= "SELECT * FROM Staff WHERE staff_id=?";
        return jdbcTemplate.queryForObject(sql,new StaffRowMapper(),profileId);
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
            ps.setDate(6, java.sql.Date.valueOf(dateOfBirth));
            ps.setString(7, gender);
            ps.setString(8, emergencyContactFirstName);
            ps.setString(9, emergencyContactLastName);
            ps.setString(10, emergencyContactNo);
            ps.setObject(11, LocalDateTime.now());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }

    public void createStaff(String employee_code, String firstName, String lastName, String email, String password, String phone, Date joining_date, String role, BigDecimal salary) {
        try {
            String sql = "INSERT INTO Staff (employee_code, first_name, last_name, email, password, phone, " +
                    "joining_date, role, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, employee_code);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, email);
                ps.setString(5, password);
                ps.setString(6, phone);
                ps.setDate(7, (java.sql.Date) joining_date);
                ps.setString(8, role);
                ps.setBigDecimal(9, salary);
                return ps;
            }, keyHolder);

        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public Integer createVendor(String vendorName, String serviceType, String contactPersonFirstName,
                                String contactPersonLastName, String email, String password, String phone,
                                String city, String amountDue, String accountNo, String ifscCode, String pin,
                                String state, String streetName) {
        String sql = "INSERT INTO Vendor (" +
                "vendor_name, service_type, contact_person_first_name, contact_person_last_name, " +
                "email, password, phone, street_name, city, state, pin, amt_due, account_no, ifsc_code, status" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

    //complete update (except id, email and password)
    public void updateCustomer(int profileId, SignupRequest profile) {
        String sql = "UPDATE Customer SET first_name=?, last_name=?, phone=?, emergency_contact_first_name=?,emergency_contact_last_name=?,date_of_birth=?,gender=?,emergency_contact_no=? WHERE customer_id=?";
        jdbcTemplate.update(sql, profile.getFirstName(), profile.getLastName(), profile.getPhone(), profile.getEmergencyContactFirstName(), profile.getEmergencyContactLastName(),profile.getDateOfBirth(),profile.getGender(), profile.getEmergencyContactNo(), profileId);
    }

    //do not update amount_due
    public void updateVendor(int profileId, SignupRequest profile) {
        //System.out.println(profile);
        String sql="UPDATE Vendor SET vendor_name=?, contact_person_first_name=?,contact_person_last_name=?,phone=?,street_name=?,city=?,state=?,pin=?,account_no=?, ifsc_code=? ,status=? WHERE vendor_id=?";
        jdbcTemplate.update(sql, profile.getVendorName(), profile.getContactPersonFirstName(), profile.getContactPersonLastName(), profile.getPhone(), profile.getStreet_name(), profile.getCity(), profile.getState(), profile.getPin(),profile.getAccount_no(),profile.getIfsc_code(), profile.getStatus(), profileId);
    }

    //do not update code,role,
    public void updateStaffForAdmin(int profileId, StaffDTO theStaff) {
        System.out.println(theStaff.toString());
        String sql="UPDATE Staff SET first_name=?,last_name=?,phone=?,role=?,salary=? WHERE staff_id=?";
        jdbcTemplate.update(sql, theStaff.getFirst_name(), theStaff.getLast_name(), theStaff.getPhone(),theStaff.getRole(),theStaff.getSalary(), profileId);
    }

    public void updateStaff(int profileId,SignupRequest profile){
        String sql="UPDATE Staff SET first_name=?, last_name=?, phone=? WHERE staff_id=?";
        jdbcTemplate.update(sql,profile.getFirstName(),profile.getLastName(),profile.getPhone(),profileId);
    }


    //    password update
    public int updateCustomerPassword(Integer customerId, String newHashedPassword) {
        String sql = "UPDATE Customer SET password = ? WHERE customer_id = ?";
        return jdbcTemplate.update(sql, newHashedPassword, customerId);
    }

    public int updateVendorPassword(Integer vendorId, String newHashedPassword) {
        String sql = "UPDATE Vendor SET password = ? WHERE vendor_id = ?";
        return jdbcTemplate.update(sql, newHashedPassword, vendorId);
    }

    public int updateStaffPassword(Integer staffId, String newHashedPassword) {
        String sql = "UPDATE Staff SET password = ? WHERE staff_id = ?";
        return jdbcTemplate.update(sql, newHashedPassword, staffId);
    }

    public String getStaffRole(int userId) {
        String sql= "SELECT role FROM Staff where staff_id=?";
        return jdbcTemplate.queryForObject(sql,new Object[]{userId},String.class);
    }

    public List<VendorDTO> getAllVendors() {
        String sql="SELECT vendor_id,vendor_name, service_type, contact_person_first_name, contact_person_last_name "
                +",email, phone, street_name, city, state, pin, amt_due, account_no, ifsc_code, status "  +
                "FROM Vendor";
        return jdbcTemplate.query(sql,(rs,rowNum)->new VendorDTO(
                rs.getInt("vendor_id"),
                rs.getString("vendor_name"),
                rs.getString("service_type"),
                rs.getString("contact_person_first_name"),
                rs.getString("contact_person_last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("street_name"),
                rs.getString("city"),
                rs.getString("state"),
                rs.getString("pin"),
                rs.getBigDecimal("amt_due"),
                rs.getString("account_no"),
                rs.getString("ifsc_code"),
                rs.getString("status")
        ));
    }

    public List<StaffDTO> getAllStaff(){
        String sql="SELECT staff_id,employee_code, first_name, last_name, email, phone," +
                "joining_date, role, salary FROM Staff";
        return jdbcTemplate.query(sql,(rs,rowNum)-> new StaffDTO(
                rs.getInt("staff_id"),
                rs.getString("employee_code"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getDate("joining_date"),
                rs.getString("role"),
                rs.getBigDecimal("salary")
        ));
    }

    public void changeVendorStatus(int vendorId, String newStatus) {
        String sql = "UPDATE Vendor SET status=? WHERE vendor_id=?";
        jdbcTemplate.update(sql,newStatus,vendorId);
    }

    public String getVendorServiceType(int vendorId) {
        String sql="SELECT service_type FROM Vendor where vendor_id=?";
        return jdbcTemplate.queryForObject(sql,new Object[]{vendorId},String.class);
    }


//    public void deleteVendorById(int vendorId) {
//        String sql="DELETE FROM Vendor WHERE vendor_id=?";
//        jdbcTemplate.update(sql,vendorId);
//    }
//
//    public void deleteStaffById(int staffId) {
//        String sql="DELETE FROM Staff WHERE staff_id=?";
//        jdbcTemplate.update(sql,staffId);
//    }

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