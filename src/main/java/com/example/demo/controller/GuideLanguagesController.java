package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.GuideLanguages;
import com.example.demo.service.AuthService;
import com.example.demo.service.GuideLanguagesService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/guides/{guideId}/languages")
//@CrossOrigin(origins = "*")
public class GuideLanguagesController {

    @Autowired
    private GuideLanguagesService guideLanguageService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    // Add a new language for a guide
    @PostMapping("/")
    public ResponseEntity<?> addGuideLanguage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId,
            @RequestBody Map<String, String> payload) {

        try {
            String language=payload.get("language");
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to create guide languages.");
            }


            String serviceType = authService.getVendorServiceType(vendorId);
            if(!Objects.equals(serviceType, "Guide_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Guide_Providers are allowed to create guide languages.");
            }

            int added = guideLanguageService.addGuideLanguage(vendorId, guideId, language);
            return ResponseEntity.ok(new ApiResponse<>(true, "Language added successfully", added));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get all languages for a guide
    @GetMapping("/")
    public ResponseEntity<?> getLanguages(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId) {

        try {
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to get guide languages.");
            }


            String serviceType = authService.getVendorServiceType(vendorId);
            if(!Objects.equals(serviceType, "Guide_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Guide_Providers are allowed to get guide languages.");
            }

            List<GuideLanguages> languages = guideLanguageService.getLanguagesByGuide(vendorId, guideId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Languages fetched successfully", languages));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Delete a specific language of a guide
    @DeleteMapping("/{language}")
    public ResponseEntity<?> deleteGuideLanguage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId,
            @PathVariable String language) {

        try {
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!Objects.equals(userType, "VENDOR")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only vendors are allowed to delete guide languages.");
            }


            String serviceType = authService.getVendorServiceType(vendorId);
            if(!Objects.equals(serviceType, "Guide_Provider")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Only Guide_Providers are allowed to delete guide languages.");
            }

            int deleted = guideLanguageService.deleteGuideLanguage(vendorId, guideId, language);
            return ResponseEntity.ok(new ApiResponse<>(true, "Language deleted successfully", deleted));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
