package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.StaffDTO;
import com.example.demo.dto.VendorDTO;
import com.example.demo.model.Staff;
import com.example.demo.model.User;
import com.example.demo.model.Vendor;
import com.example.demo.service.AuthService;
import com.example.demo.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    private AuthService authService;
    private JwtUtil jwtUtil;

    public AdminController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    //Get a list of all vendors in DB.
    @GetMapping("/vendors")
    public ResponseEntity<ApiResponse<?>> getVendors (@RequestHeader("Authorization") String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            int user_id = jwtUtil.getUserIdFromToken(token);

            if (!Objects.equals(userType, "STAFF")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Not a staff member", null));
            }

            String role = authService.getStaffRole(user_id);
            if (!Objects.equals(role, "admin")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Not a admin", null));
            }

            //perform the task as from here only admin can proceed
            List<VendorDTO> vendorsList;
            vendorsList = authService.getAllVendors();

            return ResponseEntity.ok(new ApiResponse<>(true, "Fetched all vendors successfully", vendorsList));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
        }
    }

    //Get a list of all vendors in DB.
    @GetMapping("/staff")
    public ResponseEntity<ApiResponse<?>> getStaff (@RequestHeader("Authorization") String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            int user_id = jwtUtil.getUserIdFromToken(token);

            if (!Objects.equals(userType, "STAFF")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Not a staff member", null));
            }

            String role = authService.getStaffRole(user_id);
            if (!Objects.equals(role, "admin")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Not a admin", null));
            }

            //perform the task as from here only admin can proceed
            List<StaffDTO> StaffList;
            StaffList = authService.getAllStaff();

            return ResponseEntity.ok(new ApiResponse<>(true, "Fetched all vendors successfully", StaffList));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
        }
    }

//    @DeleteMapping("/vendors/{vendorId}")
//    public void deleteVendor(@PathVariable int vendorId, @RequestHeader("Authorization") String authHeader){
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new RuntimeException("No token found");
//        }
//
//        String token = authHeader.substring(7);
//        String userType = jwtUtil.getUserTypeFromToken(token);
//        int user_id  = jwtUtil.getUserIdFromToken(token);
//
//        if(!Objects.equals(userType, "STAFF")){
//            throw new RuntimeException("Only staff(Admin) can access this route");
//        }
//
//        String role = authService.getStaffRole(user_id);
//        if(!Objects.equals(role, "admin")) {
//            throw new RuntimeException("Only admin can access this route");
//        }
//
//        authService.deleteVendor(vendorId);
//        return;
//    }

//    @DeleteMapping("/staff/{staffId}")
//    public void deleteStaff(@PathVariable int staffId, @RequestHeader("Authorization") String authHeader){
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new RuntimeException("No token found");
//        }
//
//        String token = authHeader.substring(7);
//        String userType = jwtUtil.getUserTypeFromToken(token);
//        int user_id  = jwtUtil.getUserIdFromToken(token);
//
//        if(!Objects.equals(userType, "STAFF")){
//            throw new RuntimeException("Only staff(Admin) can access this route");
//        }
//
//        String role = authService.getStaffRole(user_id);
//        if(!Objects.equals(role, "admin")) {
//            throw new RuntimeException("Only admin can access this route");
//        }
//
//        String roleOfTargetPerson = authService.getStaffRole(staffId);
//        if(Objects.equals(roleOfTargetPerson, "admin")){
//            throw new RuntimeException("Can't remove a admin");
//        }
//
//        authService.deleteStaff(staffId);
//        return;
//    }


    @PostMapping("/staff")
    public ResponseEntity<? extends ApiResponse<?>> createStaff(@RequestBody Staff theStaff, @RequestHeader("Authorization") String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No token found");
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            int user_id = jwtUtil.getUserIdFromToken(token);

            if (!Objects.equals(userType, "STAFF")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Be a staff bro!", null));
            }

            String role = authService.getStaffRole(user_id);
            if (!Objects.equals(role, "admin")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Be a admin bro!", null));
            }


            Staff dbStaff = authService.createStaff(theStaff);
            dbStaff.setPassword(null);
            dbStaff.setUserType("STAFF");
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Created successfully", dbStaff));
        }
        catch (Exception e){
           return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
        }
    }

    @GetMapping("/staff/{staffId}")
    public ResponseEntity<? extends ApiResponse<?>> getStaff(@RequestHeader("Authorization") String authHeader,@PathVariable int staffId){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No token found");
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            int user_id = jwtUtil.getUserIdFromToken(token);

            if (!Objects.equals(userType, "STAFF")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Be a staff bro!", null));
            }

            String role = authService.getStaffRole(user_id);
            if (!Objects.equals(role, "admin")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Be a admin bro!", null));
            }

            User theStaff = authService.getUserByIdAndUserType(staffId, "STAFF");
            theStaff.setUserType("STAFF");
            theStaff.setPassword(null);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse<>(true, "here you go!", theStaff));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
        }
    }

    @PutMapping("/staff/{staffId}")
    public ResponseEntity<? extends ApiResponse<?>> updateStaff(@RequestHeader("Authorization") String authHeader,@PathVariable int staffId,@RequestBody StaffDTO theStaff){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("No token found");
        }

        try {
            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);
            int user_id = jwtUtil.getUserIdFromToken(token);

            if (!Objects.equals(userType, "STAFF")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Be a staff bro!", null));
            }


            String role = authService.getStaffRole(user_id);
            if (!Objects.equals(role, "admin")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Be a admin bro!", null));
            }


            authService.updateStaff(staffId, theStaff);
            Staff dbStaff = authService.getStaffById(staffId);
            dbStaff.setPassword(null);
            dbStaff.setUserType("STAFF");
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "Updated successfully", dbStaff));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,e.getMessage(),null));
        }

    }





}
