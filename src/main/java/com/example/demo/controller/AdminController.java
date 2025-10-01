package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.StaffDTO;
import com.example.demo.dto.VendorDTO;
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

        String token = authHeader.substring(7);
        String userType = jwtUtil.getUserTypeFromToken(token);
        int user_id  = jwtUtil.getUserIdFromToken(token);

        if(!Objects.equals(userType, "STAFF")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Not a staff member",null));
        }

        String role = authService.getStaffRole(user_id);
        if(!Objects.equals(role, "admin")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Not a admin",null));
        }

        //perform the task as from here only admin can proceed
        List<VendorDTO> vendorsList;
        vendorsList=authService.getAllVendors();

        return ResponseEntity.ok(new ApiResponse<>(true,"Fetched all vendors successfully",vendorsList));
    }

    //Get a list of all vendors in DB.
    @GetMapping("/staff")
    public ResponseEntity<ApiResponse<?>> getStaff (@RequestHeader("Authorization") String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Missing or invalid Authorization header", null));
        }

        String token = authHeader.substring(7);
        String userType = jwtUtil.getUserTypeFromToken(token);
        int user_id  = jwtUtil.getUserIdFromToken(token);

        if(!Objects.equals(userType, "STAFF")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Not a staff member",null));
        }

        String role = authService.getStaffRole(user_id);
        if(!Objects.equals(role, "admin")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Not a admin",null));
        }

        //perform the task as from here only admin can proceed
        List<StaffDTO> StaffList;
        StaffList=authService.getAllStaff();

        return ResponseEntity.ok(new ApiResponse<>(true,"Fetched all vendors successfully",StaffList));
    }

    @DeleteMapping("/vendors/{vendorId}")
    public void deleteVendor(@PathVariable int vendorId, @RequestHeader("Authorization") String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new Error("No token found");
        }

        String token = authHeader.substring(7);
        String userType = jwtUtil.getUserTypeFromToken(token);
        int user_id  = jwtUtil.getUserIdFromToken(token);

        if(!Objects.equals(userType, "STAFF")){
            throw new Error("Only staff(Admin) can access this route");
        }

        String role = authService.getStaffRole(user_id);
        if(!Objects.equals(role, "admin")) {
            throw new Error("Only admin can access this route");
        }

        authService.deleteVendor(vendorId);
        return;
    }

    @DeleteMapping("/staff/{staffId}")
    public void deleteStaff(@PathVariable int staffId, @RequestHeader("Authorization") String authHeader){
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new Error("No token found");
        }

        String token = authHeader.substring(7);
        String userType = jwtUtil.getUserTypeFromToken(token);
        int user_id  = jwtUtil.getUserIdFromToken(token);

        if(!Objects.equals(userType, "STAFF")){
            throw new Error("Only staff(Admin) can access this route");
        }

        String role = authService.getStaffRole(user_id);
        if(!Objects.equals(role, "admin")) {
            throw new Error("Only admin can access this route");
        }

        String roleOfTargetPerson = authService.getStaffRole(staffId);
        if(Objects.equals(roleOfTargetPerson, "admin")){
            throw new RuntimeException("Can't remove a admin");
        }

        authService.deleteStaff(staffId);
        return;
    }




}
