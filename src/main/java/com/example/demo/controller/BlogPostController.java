package com.example.demo.controller;

import com.example.demo.dto.BlogPostDto;
import com.example.demo.model.BlogPost;
import com.example.demo.service.BlogPostService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "*")
public class BlogPostController {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private JwtUtil jwtUtil;

    public BlogPostController(BlogPostService blogPostService, JwtUtil jwtUtil) {
        this.blogPostService = blogPostService;
        this.jwtUtil = jwtUtil;
    }

    // Staff adds a new blog post
    @PostMapping("/add")
    public ResponseEntity<String> addBlog(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody BlogPostDto dto) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!"STAFF".equalsIgnoreCase(userType)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only staff members can add blogs");
            }

            Integer staffId = jwtUtil.getUserIdFromToken(token);
            Integer blogId = blogPostService.addBlog(dto, staffId);

            return ResponseEntity.ok("Blog created successfully with ID: " + blogId);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating blog: " + e.getMessage());
        }
    }

    // Get a single blog by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable("id") Integer blogId) {
        try {
            BlogPost blog = blogPostService.getBlogById(blogId);
            return ResponseEntity.ok(blog);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get all blogs
    @GetMapping
    public ResponseEntity<List<BlogPost>> getAllBlogs() {
        List<BlogPost> blogs = blogPostService.getAllBlogs();
        return ResponseEntity.ok(blogs);
    }

    // Update a blog post (staff only)
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBlog(@RequestHeader("Authorization") String authHeader,
                                             @PathVariable("id") Integer blogId,
                                             @RequestBody BlogPostDto dto) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!"STAFF".equalsIgnoreCase(userType)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only staff members can update blogs");
            }

            int updated = blogPostService.updateBlog(blogId, dto);
            if (updated == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found or not updated");
            }

            return ResponseEntity.ok("Blog updated successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating blog: " + e.getMessage());
        }
    }

    // Delete a blog post (staff only)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBlog(@RequestHeader("Authorization") String authHeader,
                                             @PathVariable("id") Integer blogId) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header");
            }

            String token = authHeader.substring(7);
            String userType = jwtUtil.getUserTypeFromToken(token);

            if (!"STAFF".equalsIgnoreCase(userType)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only staff members can delete blogs");
            }

            int deleted = blogPostService.deleteBlog(blogId);
            if (deleted == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found or not deleted");
            }

            return ResponseEntity.ok("Blog deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting blog: " + e.getMessage());
        }
    }
}
