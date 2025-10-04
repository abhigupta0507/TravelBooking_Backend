package com.example.demo.service;

import com.example.demo.dao.AuthDao;
import com.example.demo.dto.*;
import com.example.demo.model.Customer;
import com.example.demo.model.Staff;
import com.example.demo.model.User;
import com.example.demo.model.Vendor;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AuthService {

    @Autowired
    private AuthDao authDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthService(AuthDao authDao, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authDao = authDao;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    private boolean isValidUserType(String userType) {
        return userType != null &&
                (userType.equalsIgnoreCase("CUSTOMER") ||
                        userType.equalsIgnoreCase("VENDOR") ||
                        userType.equalsIgnoreCase("STAFF"));
    }

    public AuthResponse signup(SignupRequest request) {
        try{


            // Check if email already exists
            if (authDao.emailExists(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }

            //Only customer and vendor
            if (!isValidUserType(request.getUserType())) {
                throw new RuntimeException("Invalid user type");
            }

            // Encrypt password
            String hashedPassword = passwordEncoder.encode(request.getPassword());

            Integer userId = null;
            String userType = request.getUserType().toUpperCase();

            switch (userType) {
                case "CUSTOMER":
                    userId = authDao.createCustomer(request.getFirstName(), request.getLastName(), request.getEmail(),
                            hashedPassword, request.getPhone(), request.getDateOfBirth(), request.getGender(),
                            request.getEmergencyContactFirstName(), request.getEmergencyContactLastName(),
                            request.getEmergencyContactNo(),
                            "1"
                    );
                    break;
                case "VENDOR":
                    userId = authDao.createVendor(
                            request.getVendorName(), request.getServiceType(), request.getContactPersonFirstName(),
                            request.getContactPersonLastName(), request.getEmail(), hashedPassword, request.getPhone(),
                            request.getCity(), request.getAmount_due(), request.getAccount_no(), request.getIfsc_code(),
                            request.getPin(), request.getState(), request.getStreet_name()
                    );
                    break;
            }

            if (userId == null) {
                throw new RuntimeException("Failed to create user");
            }

            // Generate tokens
            String accessToken = jwtUtil.generateAccessToken(userId, request.getEmail(), userType);
            String refreshToken = jwtUtil.generateRefreshToken(userId, request.getEmail(), userType);

            return new AuthResponse(accessToken, refreshToken, userType, userId, request.getEmail());
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    //analogous to signup for staff
    public Staff createStaff(Staff theStaff) {
        try {
            System.out.println(theStaff.getSalary());
            String hashedPassword = passwordEncoder.encode(theStaff.getPassword());
            authDao.createStaff(theStaff.getEmployee_code(), theStaff.getFirst_name(), theStaff.getLast_name(), theStaff.getEmail(),
                    hashedPassword, theStaff.getPhone(), theStaff.getJoining_date(), theStaff.getRole(), theStaff.getSalary());
            return authDao.findStaffByEmail(theStaff.getEmail());
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public AuthResponse login(String email, String password, String userType) {

        try {
            //customer vendor and staff are allowed to login
            if (!isValidUserType(userType)) {
                throw new RuntimeException("Invalid user type");
            }

            switch (userType) {
                case "CUSTOMER" -> {
                    Customer theCustomer = authDao.findCustomerByEmail(email);
                    if (theCustomer == null) {
                        throw new RuntimeException("Customer not found");
                    }

                    // Use password encoder to verify password
                    if (!passwordEncoder.matches(password, theCustomer.getPassword())) {
                        throw new RuntimeException("Invalid password");
                    }

                    String accessToken = jwtUtil.generateAccessToken(theCustomer.getCustomer_id(), theCustomer.getEmail(), "CUSTOMER");
                    String refreshToken = jwtUtil.generateRefreshToken(theCustomer.getCustomer_id(), theCustomer.getEmail(), "CUSTOMER");

                    return new AuthResponse(accessToken, refreshToken, "CUSTOMER", theCustomer.getCustomer_id(), theCustomer.getEmail());
                }
                case "VENDOR" -> {
                    Vendor theVendor = authDao.findVendorByEmail(email);
                    if (theVendor == null) {
                        throw new RuntimeException("Vendor not found");
                    }

                    // Use password encoder to verify password
                    if (!passwordEncoder.matches(password, theVendor.getPassword())) {
                        throw new RuntimeException("Invalid password");
                    }

                    String accessToken = jwtUtil.generateAccessToken(theVendor.getVendor_id(), theVendor.getEmail(), "VENDOR");
                    String refreshToken = jwtUtil.generateRefreshToken(theVendor.getVendor_id(), theVendor.getEmail(), "VENDOR");

                    return new AuthResponse(accessToken, refreshToken, "VENDOR", theVendor.getVendor_id(), theVendor.getEmail());
                }
                case "STAFF" -> {
                    Staff theStaff = authDao.findStaffByEmail(email);
                    if (theStaff == null) {
                        throw new RuntimeException("Staff not found");
                    }

                    // Use password encoder to verify password
                    if (!passwordEncoder.matches(password, theStaff.getPassword())) {
                        throw new RuntimeException("Invalid password");
                    }

                    String accessToken = jwtUtil.generateAccessToken(theStaff.getStaff_id(), theStaff.getEmail(), "STAFF");
                    String refreshToken = jwtUtil.generateRefreshToken(theStaff.getStaff_id(), theStaff.getEmail(), "STAFF");

                    return new AuthResponse(accessToken, refreshToken, "STAFF", theStaff.getStaff_id(), theStaff.getEmail());
                }
                default -> throw new RuntimeException("Invalid user type for login");
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public AuthResponse refreshToken(String refreshToken) {
        try {
            if (!jwtUtil.isValidToken(refreshToken)) {
                throw new RuntimeException("Invalid refresh token");
            }

            String email = jwtUtil.getEmailFromToken(refreshToken);
            Integer userId = jwtUtil.getUserIdFromToken(refreshToken);
            String userType = jwtUtil.getUserTypeFromToken(refreshToken);

            if (Objects.equals(userType, "CUSTOMER")) {
                Customer theCustomer = authDao.findCustomerByEmail(email);
                if (theCustomer == null) {
                    throw new RuntimeException("Customer not found");
                }

                String accessToken = jwtUtil.generateAccessToken(theCustomer.getCustomer_id(), theCustomer.getEmail(), "CUSTOMER");
                String newRefreshToken = jwtUtil.generateRefreshToken(theCustomer.getCustomer_id(), theCustomer.getEmail(), "CUSTOMER");

                return new AuthResponse(accessToken, newRefreshToken, "CUSTOMER", theCustomer.getCustomer_id(), theCustomer.getEmail());
            } else if (Objects.equals(userType, "VENDOR")) {
                Vendor theVendor = authDao.findVendorByEmail(email);
                if (theVendor == null) {
                    throw new RuntimeException("Vendor not found");
                }

                String accessToken = jwtUtil.generateAccessToken(theVendor.getVendor_id(), theVendor.getEmail(), "VENDOR");
                String newRefreshToken = jwtUtil.generateRefreshToken(theVendor.getVendor_id(), theVendor.getEmail(), "VENDOR");

                return new AuthResponse(accessToken, newRefreshToken, "VENDOR", theVendor.getVendor_id(), theVendor.getEmail());
            } else if (Objects.equals(userType, "STAFF")) {

                Staff theStaff = authDao.findStaffByEmail(email);
                if (theStaff == null) {
                    throw new RuntimeException("Staff not found");
                }
                String accessToken = jwtUtil.generateAccessToken(theStaff.getStaff_id(), theStaff.getEmail(), "STAFF");
                String newRefreshToken = jwtUtil.generateRefreshToken(theStaff.getStaff_id(), theStaff.getEmail(), "STAFF");

                return new AuthResponse(accessToken, newRefreshToken, "STAFF", theStaff.getStaff_id(), theStaff.getEmail());
            } else {
                throw new RuntimeException("Invalid user type in token");
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //Functions to find users by email or id
    public User getUserByEmailAndType(String email, String userType) {
        return authDao.findUserByEmailAndType(email, userType);
    }

    public User getUserByIdAndUserType(int profileId,String userType){
        try {
            if (Objects.equals(userType, "CUSTOMER")) {
                return authDao.findCustomerById(profileId);
            } else if (Objects.equals(userType, "VENDOR")) {
                return authDao.findVendorById(profileId);
            } else if (Objects.equals(userType, "STAFF")) {
                return authDao.findStaffById(profileId);
            }
            return new User();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public String getStaffRole(int userId) {
        return authDao.getStaffRole(userId);
    }

    //Returns all vendors listed in DB avoiding their passwords
    public List<VendorDTO> getAllVendors() {
        return authDao.getAllVendors();
    }

    public List<StaffDTO> getAllStaff(){
        return authDao.getAllStaff();
    }

    public User updateProfile(int profileId, String userType, SignupRequest profile) {
        // update all fields
        if(Objects.equals(userType, "CUSTOMER")){
            authDao.updateCustomer(profileId,profile);
            return authDao.findCustomerById(profileId);
        }
        else if(Objects.equals(userType, "VENDOR")){
            //System.out.println(profile);
            authDao.updateVendor(profileId,profile);
            return authDao.findVendorById(profileId);
        }
        else if(Objects.equals(userType,"STAFF")){
            authDao.updateStaff(profileId,profile);
            System.out.println(profile);
            return authDao.findStaffById(profileId);
        }
        else{
            throw new RuntimeException("INVALID USERTYPE");
        }
    }

    public void updateStaffForAdmin(int profileId,StaffDTO theStaff){
        try{
            authDao.updateStaffForAdmin(profileId,theStaff);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public Customer getCustomerById(int customerId){
        try{
            return authDao.findCustomerById(customerId);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Vendor getVendorById(int vendorId){
        try{
            return authDao.findVendorById(vendorId);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Staff getStaffById(int staffId){
        try{
            return authDao.findStaffById(staffId);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public void changePassword(Integer userId, String userType, PasswordChangeRequestDto request) {
        // Step 1: Validate that the new password and confirmation password match

        if (!Objects.equals(request.getNewPassword(), request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirmation password do not match.");
        }

        String userTypeUpper = userType.toUpperCase();
        String storedHashedPassword;

        // Step 2: Find the user and get their current stored password
        try {
            switch (userTypeUpper) {
                case "CUSTOMER":
                    Customer customer = authDao.findCustomerById(userId);
                    if (customer == null) {
                        throw new RuntimeException("User not found with the specified email and type.");
                    }
                    storedHashedPassword = customer.getPassword();
                    break;
                case "VENDOR":
                    Vendor vendor = authDao.findVendorById(userId);
                    if (vendor == null) {
                        throw new RuntimeException("User not found with the specified email and type.");
                    }
                    storedHashedPassword = vendor.getPassword();
                    break;
                case "STAFF":
                    Staff staff = authDao.findStaffById(userId);
                    if (staff == null) {
                        throw new RuntimeException("User not found with the specified email and type.");
                    }
                    storedHashedPassword = staff.getPassword();
                    break;
                default:
                    throw new RuntimeException("Invalid user type for password change.");
            }
        } catch (EmptyResultDataAccessException e) {
            // This block now correctly handles the "user not found" case
            throw new RuntimeException("User not found with the specified ID and type.");
        }

        // Step 3: Verify if the provided 'currentPassword' is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), storedHashedPassword)) {
            throw new RuntimeException("Incorrect current password provided.");
        }

        // Step 4: Encrypt the new password
        String newHashedPassword = passwordEncoder.encode(request.getNewPassword());

        // Step 5: Update the password in the database for the correct user
        // Note: You will need to create these update methods in your AuthDao
        try {
            int success = switch (userTypeUpper) {
                case "CUSTOMER" -> authDao.updateCustomerPassword(userId, newHashedPassword);
                case "VENDOR" -> authDao.updateVendorPassword(userId, newHashedPassword);
                case "STAFF" -> authDao.updateStaffPassword(userId, newHashedPassword);
                default -> throw new RuntimeException("Given UserType is not one of viable choices!");
            };
            if(success==0){
                throw new RuntimeException("Database update failed!");
            }
        }
        catch(RuntimeException e){
            throw new RuntimeException("Database update failed!");
        }
    }

    public void changeVendorStatus(int vendor_id,String newStatus){
        try{
            authDao.changeVendorStatus(vendor_id,newStatus);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    //instead of deleting a vendor we will allow vendor to change his status only.
//    public void deleteVendor(int vendorId) {
//        authDao.deleteVendorById(vendorId);
//    }

    //probably delete this one we don't need it
//    public void deleteStaff(int staffId) {
//        authDao.deleteStaffById(staffId);
//    }

}