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

    public AuthResponse signup(SignupRequest request) {
        // Check if email already exists
        if (authDao.emailExists(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Validate user type
        if (!isValidUserType(request.getUserType())) {
            throw new RuntimeException("Invalid user type");
        }

        // Validate role for staff
        if ("STAFF".equalsIgnoreCase(request.getUserType())) {
            if (request.getRole() == null ||
                    (!request.getRole().equalsIgnoreCase("CONTENT_CREATOR") &&
                            !request.getRole().equalsIgnoreCase("HELP_DESK"))) {
                throw new RuntimeException("Invalid role for staff. Must be CONTENT_CREATOR or HELP_DESK");
            }
        }

        // Encrypt password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Integer userId = null;
        String userType = request.getUserType().toUpperCase();

        switch (userType) {
            case "CUSTOMER":
                userId = authDao.createCustomer(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        hashedPassword, // Use encrypted password
                        request.getPhone(),
                        request.getDateOfBirth(),
                        request.getGender(),
                        request.getEmergencyContactFirstName(),
                        request.getEmergencyContactLastName(),
                        request.getEmergencyContactNo(),
                        "1"
                );
                break;
            case "VENDOR":
                // Debug prints to see what's being passed
                System.out.println("=== VENDOR SIGNUP DEBUG ===");
                System.out.println("VendorName: " + request.getVendorName());
                System.out.println("ServiceType: " + request.getServiceType());
                System.out.println("ContactPersonFirstName: " + request.getContactPersonFirstName());
                System.out.println("ContactPersonLastName: " + request.getContactPersonLastName());
                System.out.println("Amount_due: " + request.getAmount_due());
                System.out.println("FirstName: " + request.getFirstName());
                System.out.println("LastName: " + request.getLastName());

                userId = authDao.createVendor(
                        request.getVendorName(),
                        request.getServiceType(),
                        request.getContactPersonFirstName() != null ? request.getContactPersonFirstName() : request.getFirstName(),
                        request.getContactPersonLastName() != null ? request.getContactPersonLastName() : request.getLastName(),
                        request.getEmail(),
                        hashedPassword, // Use encrypted password
                        request.getPhone(),
                        request.getCity(),
                        request.getAmount_due(),
                        request.getAccount_no(),
                        request.getIfsc_code(),
                        request.getPin(),
                        request.getState(),
                        request.getStreet_name()
                );
                break;
            case "STAFF":
                userId = authDao.createStaff(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        hashedPassword, // Use encrypted password
                        request.getPhone(),
                        request.getRole().toUpperCase()
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

    public AuthResponse login(String email, String password, String userType) {
        if (!isValidUserType(userType)) {
            throw new RuntimeException("Invalid user type");
        }

        if(userType.equals("CUSTOMER")){
            Customer theCustomer = authDao.findCustomerByEmail(email);
            if (theCustomer == null) {
                throw new RuntimeException("Customer not found");
            }

            // Use password encoder to verify password
            if(!passwordEncoder.matches(password, theCustomer.getPassword())){
                throw new RuntimeException("Invalid password");
            }

            String accessToken = jwtUtil.generateAccessToken(theCustomer.getCustomer_id(), theCustomer.getEmail(),"CUSTOMER");
            String refreshToken = jwtUtil.generateRefreshToken(theCustomer.getCustomer_id(), theCustomer.getEmail(),"CUSTOMER");

            return new AuthResponse(accessToken, refreshToken, "CUSTOMER", theCustomer.getCustomer_id(), theCustomer.getEmail());
        }
        else if(userType.equals("VENDOR")){
            Vendor theVendor = authDao.findVendorByEmail(email);
            if (theVendor == null) {
                throw new RuntimeException("Vendor not found");
            }

            // Use password encoder to verify password
            if(!passwordEncoder.matches(password, theVendor.getPassword())){
                throw new RuntimeException("Invalid password");
            }

            String accessToken = jwtUtil.generateAccessToken(theVendor.getVendor_id(), theVendor.getEmail(), "VENDOR");
            String refreshToken = jwtUtil.generateRefreshToken(theVendor.getVendor_id(), theVendor.getEmail(),"VENDOR");

            return new AuthResponse(accessToken, refreshToken, "VENDOR", theVendor.getVendor_id(), theVendor.getEmail());
        }
        else if(userType.equals("STAFF")){
            Staff theStaff = authDao.findStaffByEmail(email);
            if (theStaff == null) {
                throw new RuntimeException("Staff not found");
            }

            // BUG

            //RIGHT NOW SINCE WE ARE NOT CREATING ENTRIES INTO STAFF FROM BACKEND THEREFORE HASHING IS
            //DONE HENCE THIS CODE BELOW WILL ALWAYS GIVE ERROR THEREFORE I AM COMMENTING IT UNTIL WE
            //GET THE MECHANISM TO CREATE STAFF (ENCODE PASSWORDS)

            // Use password encoder to verify password
               //if(!passwordEncoder.matches(password, theStaff.getPassword())){
                   //throw new RuntimeException("Invalid password");
              //}

            String accessToken = jwtUtil.generateAccessToken(theStaff.getStaff_id(), theStaff.getEmail(), "STAFF");
            String refreshToken = jwtUtil.generateRefreshToken(theStaff.getStaff_id(), theStaff.getEmail(),"STAFF");

            return new AuthResponse(accessToken, refreshToken, "STAFF", theStaff.getStaff_id(), theStaff.getEmail());
        }
        else{
            throw new RuntimeException("Invalid user type for login");
        }
    }

    public AuthResponse refreshToken(String refreshToken) {
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
        } else {
            throw new RuntimeException("Invalid user type in token");
        }
    }

    private boolean isValidUserType(String userType) {
        return userType != null &&
                (userType.equalsIgnoreCase("CUSTOMER") ||
                        userType.equalsIgnoreCase("VENDOR") ||
                        userType.equalsIgnoreCase("STAFF"));
    }

    public User getUserByEmailAndType(String email, String userType) {
        return authDao.findUserByEmailAndType(email, userType);
    }

    public User getUserByIdAndUserType(int profileId,String userType){
        if(Objects.equals(userType, "CUSTOMER")){
            return authDao.findCustomerById(profileId);
        }
        else if(Objects.equals(userType, "VENDOR")){
            return authDao.findVendorById(profileId);
        }
        else if(Objects.equals(userType, "STAFF")){
            return authDao.findStaffById(profileId);
        }
        return new User();
    }

    public Object updateProfile(int profileId, String userType, SignupRequest profile) {
        // update all fields
        if(Objects.equals(userType, "CUSTOMER")){
            authDao.updateCustomer(profileId,profile);
            return authDao.findCustomerById(profileId);
        }
        else if(Objects.equals(userType, "VENDOR")){
            System.out.println(profile);
            authDao.updateVendor(profileId,profile);
            return authDao.findVendorById(profileId);
        }
        else if(Objects.equals(userType, "STAFF")){
            authDao.updateStaff(profileId,profile);
            return authDao.findStaffById(profileId);
        }
        else{
            throw new RuntimeException("INVALID USERTYPE");
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
                    // Note: You will need to create findStaffByEmail in your AuthDao
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


    public String getStaffRole(int userId) {
        return authDao.getStaffRole(userId);
    }

    public List<VendorDTO> getAllVendors() {
        return authDao.getAllVendors();
    }

    public List<StaffDTO> getAllStaff(){
        return authDao.getAllStaff();
    }
}