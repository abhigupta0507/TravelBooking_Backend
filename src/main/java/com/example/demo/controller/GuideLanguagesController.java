package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.model.GuideLanguages;
import com.example.demo.service.GuideLanguagesService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/guides/{guideId}/languages")
//@CrossOrigin(origins = "*")
public class GuideLanguagesController {

    @Autowired
    private GuideLanguagesService guideLanguageService;

    @Autowired
    private JwtUtil jwtUtil;

    // Add a new language for a guide
    @PostMapping("/")
    public ResponseEntity<ApiResponse<Integer>> addGuideLanguage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId,
            @RequestBody Map<String, String> payload) {

        try {
            String language=payload.get("language");
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            int added = guideLanguageService.addGuideLanguage(vendorId, guideId, language);
            return ResponseEntity.ok(new ApiResponse<>(true, "Language added successfully", added));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Get all languages for a guide
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<GuideLanguages>>> getLanguages(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId) {

        try {
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            List<GuideLanguages> languages = guideLanguageService.getLanguagesByGuide(vendorId, guideId);
            return ResponseEntity.ok(new ApiResponse<>(true, "Languages fetched successfully", languages));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // Delete a specific language of a guide
    @DeleteMapping("/{language}")
    public ResponseEntity<ApiResponse<Integer>> deleteGuideLanguage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer guideId,
            @PathVariable String language) {

        try {
            String token = authHeader.substring(7);
            Integer vendorId = jwtUtil.getUserIdFromToken(token);

            int deleted = guideLanguageService.deleteGuideLanguage(vendorId, guideId, language);
            return ResponseEntity.ok(new ApiResponse<>(true, "Language deleted successfully", deleted));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
